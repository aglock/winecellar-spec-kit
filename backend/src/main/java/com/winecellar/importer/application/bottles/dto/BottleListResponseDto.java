package com.winecellar.importer.application.bottles.dto;

import java.util.List;

public record BottleListResponseDto(
    int page,
    int size,
    long total,
    List<BottleListItemDto> items
) {
}
