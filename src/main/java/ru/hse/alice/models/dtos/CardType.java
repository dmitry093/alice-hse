package ru.hse.alice.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum CardType {
    @JsonProperty("BigImage")
    BIG_IMAGE,
    @JsonProperty("ItemsList")
    ITEMS_LIST;
}
