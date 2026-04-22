package com.winecellar.importer.application.imports;

import com.winecellar.importer.api.imports.dto.ImportSummaryResponse;
import com.winecellar.importer.api.imports.dto.SkippedRowResponse;
import com.winecellar.importer.infrastructure.csv.CommonsCsvParser;
import com.winecellar.importer.infrastructure.mongo.cellar.CellarDocument;
import com.winecellar.importer.infrastructure.mongo.cellar.CellarRepository;
import com.winecellar.importer.infrastructure.mongo.cellar.CompartmentDocument;
import com.winecellar.importer.infrastructure.mongo.cellar.CompartmentRepository;
import com.winecellar.importer.infrastructure.mongo.inventory.BottleDocument;
import com.winecellar.importer.infrastructure.mongo.inventory.BottleRepository;
import com.winecellar.importer.infrastructure.mongo.inventory.WineDocument;
import com.winecellar.importer.infrastructure.mongo.inventory.WineRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WineBottleCsvImportService {

  private static final int NON_VINTAGE_SENTINEL = 1001;
  private static final int NO_CONSUME_WINDOW_SENTINEL = 9999;

  private final CommonsCsvParser csvParser;
  private final CanonicalReferenceResolver canonicalReferenceResolver;
  private final CellarRepository cellarRepository;
  private final CompartmentRepository compartmentRepository;
  private final WineRepository wineRepository;
  private final BottleRepository bottleRepository;
  private final ImportEventRecorder importEventRecorder;

  public WineBottleCsvImportService(
      CommonsCsvParser csvParser,
      CanonicalReferenceResolver canonicalReferenceResolver,
      CellarRepository cellarRepository,
      CompartmentRepository compartmentRepository,
      WineRepository wineRepository,
      BottleRepository bottleRepository,
      ImportEventRecorder importEventRecorder
  ) {
    this.csvParser = csvParser;
    this.canonicalReferenceResolver = canonicalReferenceResolver;
    this.cellarRepository = cellarRepository;
    this.compartmentRepository = compartmentRepository;
    this.wineRepository = wineRepository;
    this.bottleRepository = bottleRepository;
    this.importEventRecorder = importEventRecorder;
  }

  public ImportSummaryResponse importCsv(MultipartFile file) throws IOException {
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("CSV file is required");
    }

    CellarDocument cellar = cellarRepository.findFirstByOrderByCreatedAtAsc()
        .orElseThrow(() -> new IllegalStateException("Default cellar has not been initialized"));

    CompartmentDocument compartment = compartmentRepository.findFirstByCellarIdOrderByCreatedAtAsc(cellar.getId())
        .orElseThrow(() -> new IllegalStateException("Default compartment has not been initialized"));

    List<CommonsCsvParser.CsvRow> rows = csvParser.parse(file.getInputStream());
    List<SkippedRowResponse> skippedRows = new ArrayList<>();
    int importedCount = 0;

    for (CommonsCsvParser.CsvRow row : rows) {
      try {
        importRow(row, cellar, compartment);
        importedCount++;
      } catch (Exception ex) {
        skippedRows.add(new SkippedRowResponse(row.rowNumber(), ex.getMessage()));
      }
    }

    String eventId = importEventRecorder.recordCsvImport(
        cellar.getId(),
        file.getOriginalFilename(),
        importedCount,
        skippedRows.size()
    );

    return new ImportSummaryResponse(importedCount, skippedRows.size(), skippedRows, eventId);
  }

  private void importRow(CommonsCsvParser.CsvRow row, CellarDocument cellar, CompartmentDocument compartment) {
    String wineName = requiredText(row, "Wine");
    String producerName = requiredText(row, "Producer");
    String countryName = requiredText(row, "Country");
    String regionName = requiredText(row, "Region");
    String bottleSize = defaultedText(row, "Size", "750ml");

    CanonicalReferenceResolver.ResolvedReferences references = canonicalReferenceResolver.resolve(
        producerName,
        countryName,
        regionName,
        row.value("Appellation"),
        bottleSize
    );

    WineDocument wine = new WineDocument();
    wine.setName(wineName);
    wine.setNormalizedName(CsvValueNormalizer.normalizeKey(wineName));
    wine.setProducerId(references.producerId());
    wine.setWineType(defaultedText(row, "Type", "Unknown"));
    wine.setRegionId(references.regionId());
    wine.setAppellationId(references.appellationId());
    wine.setVarietal(nullableText(row.value("Varietal")));
    WineDocument persistedWine = wineRepository.save(wine);

    BottleDocument bottle = new BottleDocument();
    bottle.setCellarId(cellar.getId());
    bottle.setCompartmentId(compartment.getId());
    bottle.setWineId(persistedWine.getId());
    bottle.setBottleSizeId(references.bottleSizeId());
    bottle.setVintage(parseRequiredInteger(row.value("Vintage"), "Vintage"));
    bottle.setQuantity(parseRequiredInteger(defaultedText(row, "Quantity", "1"), "Quantity"));
    bottle.setPending(parseRequiredInteger(defaultedText(row, "Pending", "0"), "Pending"));
    bottle.setValue(parseNullableDecimal(row.value("Value")));
    bottle.setPrice(parseNullableDecimal(row.value("Price")));
    bottle.setCurrency(defaultedText(row, "Currency", "SEK"));
    bottle.setConsumeFromYear(parseNullableYear(row.value("BeginConsume"), NO_CONSUME_WINDOW_SENTINEL));
    bottle.setConsumeUntilYear(parseNullableYear(row.value("EndConsume"), NO_CONSUME_WINDOW_SENTINEL));
    bottle.setProfessionalScore(parseNullableDecimal(row.value("PScore")));
    bottle.setCommunityScore(parseNullableDecimal(row.value("CScore")));
    bottle.setCreatedAt(Instant.now());

    Integer vintage = bottle.getVintage();
    if (vintage == null) {
      throw new IllegalArgumentException("Vintage is required");
    }
    if (vintage != NON_VINTAGE_SENTINEL && (vintage < 1000 || vintage > 2999)) {
      throw new IllegalArgumentException("Vintage must be a year or 1001 for NV");
    }

    bottleRepository.save(bottle);
  }

  private String requiredText(CommonsCsvParser.CsvRow row, String column) {
    String value = CsvValueNormalizer.normalizeText(row.value(column));
    if (value.isBlank() || CsvValueNormalizer.isUnknownOrBlank(value)) {
      throw new IllegalArgumentException(column + " is required");
    }
    return value;
  }

  private String defaultedText(CommonsCsvParser.CsvRow row, String column, String defaultValue) {
    String value = CsvValueNormalizer.normalizeText(row.value(column));
    return value.isBlank() ? defaultValue : value;
  }

  private String nullableText(String value) {
    String normalized = CsvValueNormalizer.normalizeText(value);
    return normalized.isBlank() ? null : normalized;
  }

  private Integer parseRequiredInteger(String value, String fieldName) {
    String normalized = CsvValueNormalizer.normalizeText(value);
    if (normalized.isBlank()) {
      throw new IllegalArgumentException(fieldName + " is required");
    }
    try {
      return Integer.parseInt(normalized);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException(fieldName + " must be an integer");
    }
  }

  private Integer parseNullableYear(String value, int sentinel) {
    String normalized = CsvValueNormalizer.normalizeText(value);
    if (normalized.isBlank()) {
      return null;
    }
    try {
      return CsvValueNormalizer.nullableYearFromSentinel(normalized, sentinel);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Consume year must be an integer");
    }
  }

  private BigDecimal parseNullableDecimal(String value) {
    String normalized = CsvValueNormalizer.normalizeText(value);
    if (normalized.isBlank()) {
      return null;
    }

    String canonical = normalized.replace(" ", "").replace(',', '.');
    try {
      return new BigDecimal(canonical);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Numeric value is invalid: " + value);
    }
  }
}
