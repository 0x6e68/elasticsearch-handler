package ch.zuehlke.cloudchallange.handler;

import ch.zuehlke.cloudchallange.dto.XovisEvent;
import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.aws.codestar.projecttemplates.GatewayResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ElasticSearchHandler implements RequestHandler<List<XovisEvent>, GatewayResponse> {

    private final static String ENDPOINT = "https://search-xovis-metrics-jiuuly74cjyzh7kbuicriu7uja.eu-central-1.es.amazonaws.com";
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public GatewayResponse handleRequest(final List<XovisEvent> input, final Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("got events:" + input.size());

        input.forEach(event-> sendXovisEvent(event, logger));

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return new GatewayResponse(new JSONObject().put("Output", "ElasticSearch").toString(), headers, 200);
    }

    private void sendXovisEvent(XovisEvent xovisEvent,LambdaLogger logger) {
        try {
            RestClient esClient = esClient("es", "eu-central-1");
            Map<String, Object> payload = new HashMap<>();

            payload.put("type", xovisEvent.getType().get());

            xovisEvent.getTimestamp().ifPresent(timestamp -> {
                Instant instant = Instant.ofEpochMilli(timestamp);
                LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());


                payload.put("timestamp", localDateTime.format(FORMATTER));
            });

            xovisEvent.getDirection().ifPresent(direction -> {
                payload.put("direction", direction);
            });

            xovisEvent.getCountItem().ifPresent(countItem -> {
                countItem.getName().ifPresent(name -> {
                    payload.put("count-item-name", name);
                });

                countItem.getId().ifPresent(id -> {
                    payload.put("count-item-id", id);
                });
            });

            xovisEvent.getObject().ifPresent(object -> {
                object.getId().ifPresent(id -> {
                    payload.put("object-id", id);
                });

                object.getHeight().ifPresent(height -> {
                    payload.put("object-height", height);
                });
            });


            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(payload);

            // Register a snapshot repository
            HttpEntity entity = new NStringEntity(json, ContentType.APPLICATION_JSON);
            Request request = new Request("POST", "/event/_doc");
            request.setEntity(entity);

            esClient.performRequest(request);
        } catch (IOException e) {
            logger.log("error while serializing payload " + e.getMessage());
        }
    }

    private RestClient esClient(String serviceName, String region) {
        AWS4Signer signer = new AWS4Signer();
        signer.setServiceName(serviceName);
        signer.setRegionName(region);
        return RestClient.builder(HttpHost.create(ENDPOINT)).build();
    }
}
