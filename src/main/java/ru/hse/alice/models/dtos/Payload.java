package ru.hse.alice.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Payload {
    @JsonProperty("button_command")
    private String button_command;
}
