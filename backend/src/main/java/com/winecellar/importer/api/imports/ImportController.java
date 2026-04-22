package com.winecellar.importer.api.imports;

import com.winecellar.importer.api.imports.dto.ImportSummaryResponse;
import com.winecellar.importer.application.imports.WineBottleCsvImportService;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/imports")
public class ImportController {

  private final WineBottleCsvImportService wineBottleCsvImportService;

  public ImportController(WineBottleCsvImportService wineBottleCsvImportService) {
    this.wineBottleCsvImportService = wineBottleCsvImportService;
  }

  @PostMapping(path = "/wine-bottles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ImportSummaryResponse importWineBottles(@RequestPart("file") MultipartFile file) throws IOException {
    return wineBottleCsvImportService.importCsv(file);
  }
}
