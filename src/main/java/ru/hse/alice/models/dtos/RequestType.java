package ru.hse.alice.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum RequestType {
    @JsonProperty("ButtonPressed")
    BUTTON_PRESSED,
    @JsonProperty("SimpleUtterance")
    SIMPLE_UTTERANCE;
}
