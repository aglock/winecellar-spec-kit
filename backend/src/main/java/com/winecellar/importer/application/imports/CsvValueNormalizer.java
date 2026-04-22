package com.winecellar.importer.application.imports;

import java.text.Normalizer;
import java.util.Locale;

public final class CsvValueNormalizer {

  private CsvValueNormalizer() {
  }

  public static String normalizeText(String value) {
    if (value == null) {
      return "";
    }
    String trimmed = value.trim().replaceAll("\\s+", " ");
    return Normalizer.normalize(trimmed, Normalizer.Form.NFKC);
  }

  public static String normalizeKey(String value) {
    return normalizeText(value).toLowerCase(Locale.ROOT);
  }

  public static boolean isUnknownOrBlank(String value) {
    String normalized = normalizeKey(value);
    return normalized.isBlank() || normalized.equals("unknown");
  }

  public static Integer nullableYearFromSentinel(String value, int sentinel) {
    String normalized = normalizeText(value);
    if (normalized.isBlank()) {
      return null;
    }
    int parsed = Integer.parseInt(normalized);
    return parsed == sentinel ? null : parsed;
  }
}
