package ru.hse.alice.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@AllArgsConstructor
public class Card {
    @JsonProperty("type")
    @JsonInclude(NON_NULL)
    private CardType type;

    @JsonProperty("image_id")
    private String imageId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;
}
