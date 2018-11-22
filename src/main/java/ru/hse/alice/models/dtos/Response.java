package ru.hse.alice.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
public class Response {
    @JsonProperty("text")
    private String text;

    @JsonProperty("tts")
    @JsonInclude(NON_NULL)
    private String tts;

    @JsonProperty("buttons")
    @JsonInclude(NON_NULL)
    private List<Button> buttons;

    @JsonProperty("card")
    private Card card;

    @JsonProperty("end_session")
    private boolean endSession;
}
