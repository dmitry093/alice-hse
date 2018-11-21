package ru.domru.tv.alicehse.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequestMarkup {
    @JsonProperty("dangerous_context")
    private boolean dangerousContext;
}
