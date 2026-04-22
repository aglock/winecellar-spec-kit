package com.winecellar.importer.api.bottles;

import com.winecellar.importer.application.bottles.ListDefaultCellarBottlesService;
import com.winecellar.importer.application.bottles.dto.BottleListResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cellars/default")
public class BottleListController {

  private final ListDefaultCellarBottlesService listDefaultCellarBottlesService;

  public BottleListController(ListDefaultCellarBottlesService listDefaultCellarBottlesService) {
    this.listDefaultCellarBottlesService = listDefaultCellarBottlesService;
  }

  @GetMapping("/bottles")
  public BottleListResponseDto listBottles(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "50") int size
  ) {
    return listDefaultCellarBottlesService.list(page, size);
  }
}
