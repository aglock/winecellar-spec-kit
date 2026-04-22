package com.winecellar.importer.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.winecellar.importer.api.bottles.BottleListController;
import com.winecellar.importer.application.bottles.ListDefaultCellarBottlesService;
import com.winecellar.importer.application.bottles.dto.BottleListItemDto;
import com.winecellar.importer.application.bottles.dto.BottleListResponseDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;

@WebMvcTest(BottleListController.class)
class BottleListControllerContractTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ListDefaultCellarBottlesService listDefaultCellarBottlesService;

  @Test
  void listDefaultCellarBottles_shouldReturnContractShape() throws Exception {
    BottleListItemDto item = new BottleListItemDto(
        "bottle-1",
        "Test Wine",
        "Test Producer",
        "NV",
        "Sparkling",
        "750ml",
        "France",
        "Champagne",
        2
    );

    when(listDefaultCellarBottlesService.list(0, 50))
        .thenReturn(new BottleListResponseDto(0, 50, 1, List.of(item)));

    mockMvc.perform(get("/api/cellars/default/bottles"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page").value(0))
        .andExpect(jsonPath("$.size").value(50))
        .andExpect(jsonPath("$.total").value(1))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].bottleId").value("bottle-1"))
        .andExpect(jsonPath("$.items[0].wineName").value("Test Wine"))
        .andExpect(jsonPath("$.items[0].producerName").value("Test Producer"))
        .andExpect(jsonPath("$.items[0].vintageDisplay").value("NV"))
        .andExpect(jsonPath("$.items[0].wineType").value("Sparkling"))
        .andExpect(jsonPath("$.items[0].bottleSize").value("750ml"))
        .andExpect(jsonPath("$.items[0].country").value("France"))
        .andExpect(jsonPath("$.items[0].region").value("Champagne"))
        .andExpect(jsonPath("$.items[0].quantity").value(2));
  }
}
