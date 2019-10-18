package ch.zuehlke.cloudchallange.dto;

import java.util.Optional;

public class XovisCountItem {
    private Long id;
    private String name;

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    public void setName(String name) {
        this.name = name;
    }
}
