package com.avb.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class ValidatedPageable{
    @Min(value = 0, message = "Page must be positive or zero")
    private Integer page = 0;

    @Min(value = 1, message = "Size must be positive")
    @Max(value = 100, message = "Size cannot exceed 100")
    private Integer size = 10;

    private String sort = "id";
    private Sort.Direction direction = Sort.Direction.ASC;

    public Pageable toPageable() {
        int actualPage = page != null ? page : 0;
        int actualSize = size != null ? size : 10;
        String actualSort = sort != null ? sort : "id";
        Sort.Direction actualDirection = direction != null ? direction : Sort.Direction.ASC;

        return PageRequest.of(actualPage, actualSize, Sort.by(actualDirection, actualSort));
    }

    public void setDirection(String direction) {
        if ("desc".equalsIgnoreCase(direction)) {
            this.direction = Sort.Direction.DESC;
        } else {
            this.direction = Sort.Direction.ASC;
        }
    }
}