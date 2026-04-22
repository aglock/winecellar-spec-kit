package com.winecellar.importer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "winecellar.bootstrap.enabled=false")
@AutoConfigureMockMvc
class ImportControllerContractTest {

  @Autowired
  private MockMvc mockMvc;

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
