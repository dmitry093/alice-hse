package ru.domru.tv.alicehse.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequestSession extends Session {
    @JsonProperty("new")
    private boolean isNew;

    @JsonProperty("message_id")
    private int messageId;
}
