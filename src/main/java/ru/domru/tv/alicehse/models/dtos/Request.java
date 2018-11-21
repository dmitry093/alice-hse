package ru.domru.tv.alicehse.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Request {
    @JsonProperty("command")
    private String command;

    @JsonProperty("original_utterance")
    private String originalUtterance;

    @JsonProperty("type")
    private String type;

    @JsonProperty("markup")
    private RequestMarkup markup;
}
