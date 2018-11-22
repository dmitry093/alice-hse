package ru.hse.alice.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Payload {
    @JsonProperty("lastcommand")
    private String lastCommand;
}
