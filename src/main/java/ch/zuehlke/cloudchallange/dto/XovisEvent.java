package ch.zuehlke.cloudchallange.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public class XovisEvent {
    private Long timestamp;
    private String type;
    private String direction;
    private XovisObject object;
    private XovisCountItem countItem;

    public Optional<Long> getTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public Optional<String> getDirection() {
        return Optional.ofNullable(direction);
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Optional<XovisObject> getObject() {
        return Optional.ofNullable(object);
    }

    public void setObject(XovisObject object) {
        this.object = object;
    }

    public Optional<XovisCountItem> getCountItem() {
        return Optional.ofNullable(countItem);
    }

    public void setCountItem(XovisCountItem countItem) {
        this.countItem = countItem;
    }
}
