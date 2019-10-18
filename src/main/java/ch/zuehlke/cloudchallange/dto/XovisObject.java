package ch.zuehlke.cloudchallange.dto;

import java.util.Optional;

public class XovisObject {
    private Long id;
    private Long x;
    private Long y;
    private Long height;

    public void setId(Long id) {
        this.id = id;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<Long> getX() {
        return Optional.ofNullable(x);
    }

    public Optional<Long> getY() {
        return Optional.ofNullable(y);
    }

    public Optional<Long> getHeight() {
        return Optional.ofNullable(height);
    }
}
