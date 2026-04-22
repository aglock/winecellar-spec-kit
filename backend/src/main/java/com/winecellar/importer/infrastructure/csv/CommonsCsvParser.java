package com.winecellar.importer.infrastructure.csv;

import com.winecellar.importer.application.imports.CsvValueNormalizer;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

@Component
public class CommonsCsvParser {

  public List<CsvRow> parse(InputStream inputStream) throws IOException {
    try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
         CSVParser parser = CSVFormat.DEFAULT.builder()
             .setHeader()
             .setSkipHeaderRecord(true)
             .setTrim(true)
             .build()
             .parse(reader)) {

      List<CsvRow> rows = new ArrayList<>();
      for (CSVRecord record : parser) {
        Map<String, String> normalized = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : record.toMap().entrySet()) {
          normalized.put(CsvValueNormalizer.normalizeKey(entry.getKey()), entry.getValue());
        }
        rows.add(new CsvRow((int) record.getRecordNumber() + 1, normalized));
      }
      return rows;
    }
  }

  public record CsvRow(int rowNumber, Map<String, String> values) {

    public String value(String columnName) {
      return values.getOrDefault(CsvValueNormalizer.normalizeKey(columnName), "");
    }
  }
}
