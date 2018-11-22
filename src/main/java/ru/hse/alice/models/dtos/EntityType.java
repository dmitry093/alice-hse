package ru.hse.alice.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EntityType {
    @JsonProperty("YANDEX.GEO")
    YANDEX_GEO,
    @JsonProperty("YANDEX.FIO")
    YANDEX_FIO,
    @JsonProperty("YANDEX.NUMBER")
    YANDEX_NUMBER,
    @JsonProperty("YANDEX.DATETIME")
    YANDEX_DATETIME,
}
