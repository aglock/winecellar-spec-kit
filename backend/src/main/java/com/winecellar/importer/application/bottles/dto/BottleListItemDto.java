package com.winecellar.importer.application.bottles.dto;

public record BottleListItemDto(
    String bottleId,
    String wineName,
    String producerName,
    String vintageDisplay,
    String wineType,
    String bottleSize,
    String country,
    String region,
    int quantity
) {
}
