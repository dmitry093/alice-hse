package ru.hse.alice.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.hse.alice.models.Payload;

@Data
public class Request {
    @JsonProperty("command")
    private String command;

    @JsonProperty("nlu")
    private Nlu nlu;

    @JsonProperty("original_utterance")
    private String originalUtterance;

    @JsonProperty("type")
    private String type;

    @JsonProperty("markup")
    private RequestMarkup markup;

    @JsonProperty("payload")
    private Object payload;
}
