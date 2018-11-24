package ru.hse.alice.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Entity {
    @JsonProperty("type")
    private EntityType type;

    @JsonProperty("value")
    private Object value;
}
