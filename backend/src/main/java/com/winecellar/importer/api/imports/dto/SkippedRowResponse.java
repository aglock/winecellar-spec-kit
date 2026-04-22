package com.winecellar.importer.api.imports.dto;

public record SkippedRowResponse(
    int rowNumber,
    String reason
) {
}
