package com.winecellar.importer.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.winecellar.importer.application.bottles.ListDefaultCellarBottlesService;
import com.winecellar.importer.application.bottles.dto.BottleListResponseDto;
import com.winecellar.importer.infrastructure.mongo.cellar.CellarDocument;
import com.winecellar.importer.infrastructure.mongo.cellar.CellarRepository;
import com.winecellar.importer.infrastructure.mongo.cellar.CompartmentDocument;
import com.winecellar.importer.infrastructure.mongo.cellar.CompartmentRepository;
import com.winecellar.importer.infrastructure.mongo.inventory.BottleDocument;
import com.winecellar.importer.infrastructure.mongo.inventory.BottleRepository;
import com.winecellar.importer.infrastructure.mongo.inventory.WineDocument;
import com.winecellar.importer.infrastructure.mongo.inventory.WineRepository;
import com.winecellar.importer.infrastructure.mongo.reference.BottleSizeDocument;
import com.winecellar.importer.infrastructure.mongo.reference.BottleSizeRepository;
import com.winecellar.importer.infrastructure.mongo.reference.CountryDocument;
import com.winecellar.importer.infrastructure.mongo.reference.CountryRepository;
import com.winecellar.importer.infrastructure.mongo.reference.ProducerDocument;
import com.winecellar.importer.infrastructure.mongo.reference.ProducerRepository;
import com.winecellar.importer.infrastructure.mongo.reference.RegionDocument;
import com.winecellar.importer.infrastructure.mongo.reference.RegionRepository;
import com.winecellar.importer.support.IntegrationTestBase;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BottleListIntegrationTest extends IntegrationTestBase {

  @Autowired
  private ListDefaultCellarBottlesService listDefaultCellarBottlesService;

  @Autowired
  private CellarRepository cellarRepository;

  @Autowired
  private CompartmentRepository compartmentRepository;

  @Autowired
  private BottleRepository bottleRepository;

  @Autowired
  private WineRepository wineRepository;

  @Autowired
  private ProducerRepository producerRepository;

  @Autowired
  private RegionRepository regionRepository;

  @Autowired
  private CountryRepository countryRepository;

  @Autowired
  private BottleSizeRepository bottleSizeRepository;

  @BeforeEach
  void clearData() {
    bottleRepository.deleteAll();
    wineRepository.deleteAll();
    bottleSizeRepository.deleteAll();
    producerRepository.deleteAll();
    regionRepository.deleteAll();
    countryRepository.deleteAll();
    compartmentRepository.deleteAll();
    cellarRepository.deleteAll();
  }

  @Test
  void listDefaultCellarBottles_shouldProjectBottleFieldsIncludingNvVintage() {
    CellarDocument cellar = new CellarDocument();
    cellar.setName("Default Cellar");
    cellar.setCreatedAt(Instant.now());
    cellar = cellarRepository.save(cellar);

    CompartmentDocument compartment = new CompartmentDocument();
    compartment.setCellarId(cellar.getId());
    compartment.setLabel("Default Compartment");
    compartment.setCreatedAt(Instant.now());
    compartmentRepository.save(compartment);

    CountryDocument country = new CountryDocument();
    country.setName("France");
    country.setNormalizedName("france");
    country = countryRepository.save(country);

    RegionDocument region = new RegionDocument();
    region.setCountryId(country.getId());
    region.setName("Champagne");
    region.setNormalizedName("champagne");
    region = regionRepository.save(region);

    ProducerDocument producer = new ProducerDocument();
    producer.setName("Test Producer");
    producer.setNormalizedName("test producer");
    producer = producerRepository.save(producer);

    BottleSizeDocument bottleSize = new BottleSizeDocument();
    bottleSize.setLabel("750ml");
    bottleSize.setNormalizedLabel("750ml");
    bottleSize.setVolumeMl(750);
    bottleSize = bottleSizeRepository.save(bottleSize);

    WineDocument wine = new WineDocument();
    wine.setName("Test Wine");
    wine.setNormalizedName("test wine");
    wine.setProducerId(producer.getId());
    wine.setWineType("Sparkling");
    wine.setRegionId(region.getId());
    wine = wineRepository.save(wine);

    BottleDocument bottle = new BottleDocument();
    bottle.setCellarId(cellar.getId());
    bottle.setCompartmentId(compartment.getId());
    bottle.setWineId(wine.getId());
    bottle.setBottleSizeId(bottleSize.getId());
    bottle.setVintage(1001);
    bottle.setQuantity(3);
    bottle.setPending(0);
    bottle.setCreatedAt(Instant.now());
    bottleRepository.save(bottle);

    BottleListResponseDto response = listDefaultCellarBottlesService.list(0, 20);

    assertEquals(1, response.total());
    assertEquals(1, response.items().size());
    assertEquals("Test Wine", response.items().getFirst().wineName());
    assertEquals("Test Producer", response.items().getFirst().producerName());
    assertEquals("NV", response.items().getFirst().vintageDisplay());
    assertEquals("Sparkling", response.items().getFirst().wineType());
    assertEquals("750ml", response.items().getFirst().bottleSize());
    assertEquals("France", response.items().getFirst().country());
    assertEquals("Champagne", response.items().getFirst().region());
    assertEquals(3, response.items().getFirst().quantity());
  }
}
