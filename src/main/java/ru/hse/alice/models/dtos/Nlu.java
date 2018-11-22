package ru.hse.alice.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Nlu {
    @JsonProperty("tokens")
    private List<String> tokens;

    @JsonProperty("entities")
    private List<Entity> entities;
}
