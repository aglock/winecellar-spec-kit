package com.winecellar.importer.api.imports.dto;

import java.util.List;

public record ImportSummaryResponse(
    int importedCount,
    int skippedCount,
    List<SkippedRowResponse> skippedRows,
    String eventId
) {
}
