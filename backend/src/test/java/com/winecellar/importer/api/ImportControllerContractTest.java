package com.winecellar.importer.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.winecellar.importer.api.imports.ImportController;
import com.winecellar.importer.api.imports.dto.ImportSummaryResponse;
import com.winecellar.importer.application.imports.WineBottleCsvImportService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    properties = "winecellar.bootstrap.enabled=false")
class ImportControllerContractTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @MockitoBean
  private WineBottleCsvImportService service;

  @BeforeEach
  void setUp() throws Exception {
    when(service.importCsv(any())).thenReturn(new ImportSummaryResponse(1, 0, List.of(), "event-1"));
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void importWineBottlesCsv_shouldReturn200_whenValidCsvIsUploaded() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
        "file",
        "bottles.csv",
        "text/csv",
        "Wine,Producer,Vintage,Country,Region\nTest Wine,Test Producer,2020,France,Bordeaux\n".getBytes()
    );

    mockMvc.perform(multipart("/api/imports/wine-bottles").file(file))
        .andExpect(status().isOk());
  }
}
