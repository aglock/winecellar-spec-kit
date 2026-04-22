package com.winecellar.importer.application.bottles;

import com.winecellar.importer.application.bottles.dto.BottleListItemDto;
import com.winecellar.importer.application.bottles.dto.BottleListResponseDto;
import com.winecellar.importer.infrastructure.mongo.cellar.CellarDocument;
import com.winecellar.importer.infrastructure.mongo.cellar.CellarRepository;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ListDefaultCellarBottlesService {

  private static final int NON_VINTAGE_SENTINEL = 1001;

  private final CellarRepository cellarRepository;
  private final BottleRepository bottleRepository;
  private final WineRepository wineRepository;
  private final ProducerRepository producerRepository;
  private final BottleSizeRepository bottleSizeRepository;
  private final RegionRepository regionRepository;
  private final CountryRepository countryRepository;

  public ListDefaultCellarBottlesService(
      CellarRepository cellarRepository,
      BottleRepository bottleRepository,
      WineRepository wineRepository,
      ProducerRepository producerRepository,
      BottleSizeRepository bottleSizeRepository,
      RegionRepository regionRepository,
      CountryRepository countryRepository
  ) {
    this.cellarRepository = cellarRepository;
    this.bottleRepository = bottleRepository;
    this.wineRepository = wineRepository;
    this.producerRepository = producerRepository;
    this.bottleSizeRepository = bottleSizeRepository;
    this.regionRepository = regionRepository;
    this.countryRepository = countryRepository;
  }

  @SuppressWarnings("null")
  public BottleListResponseDto list(int page, int size) {
    int safePage = Math.max(0, page);
    int safeSize = Math.min(200, Math.max(1, size));

    CellarDocument defaultCellar = cellarRepository.findFirstByOrderByCreatedAtAsc()
        .orElseThrow(() -> new IllegalStateException("Default cellar has not been initialized"));

    Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
    Page<BottleDocument> bottlePage = bottleRepository.findByCellarId(defaultCellar.getId(), pageable);

    Map<String, WineDocument> winesById = mapWinesById(wineRepository.findAllById(
      bottlePage.getContent().stream()
        .map(BottleDocument::getWineId)
        .filter(Objects::nonNull)
        .map(Objects::requireNonNull)
        .toList()
    ));

    Map<String, ProducerDocument> producersById = mapProducersById(producerRepository.findAllById(
      winesById.values().stream()
        .map(WineDocument::getProducerId)
        .filter(Objects::nonNull)
        .map(Objects::requireNonNull)
        .toList()
    ));

    Map<String, BottleSizeDocument> bottleSizesById = mapBottleSizesById(bottleSizeRepository.findAllById(
      bottlePage.getContent().stream()
        .map(BottleDocument::getBottleSizeId)
        .filter(Objects::nonNull)
        .map(Objects::requireNonNull)
        .toList()
    ));

    Map<String, RegionDocument> regionsById = mapRegionsById(regionRepository.findAllById(
      winesById.values().stream()
        .map(WineDocument::getRegionId)
        .filter(Objects::nonNull)
        .map(Objects::requireNonNull)
        .toList()
    ));

    Map<String, CountryDocument> countriesById = mapCountriesById(countryRepository.findAllById(
      regionsById.values().stream()
        .map(RegionDocument::getCountryId)
        .filter(Objects::nonNull)
        .map(Objects::requireNonNull)
        .toList()
    ));

    List<BottleListItemDto> items = bottlePage.getContent().stream()
        .map(bottle -> toItem(bottle, winesById, producersById, bottleSizesById, regionsById, countriesById))
        .toList();

    return new BottleListResponseDto(safePage, safeSize, bottlePage.getTotalElements(), items);
  }

  private BottleListItemDto toItem(
      BottleDocument bottle,
      Map<String, WineDocument> winesById,
      Map<String, ProducerDocument> producersById,
      Map<String, BottleSizeDocument> bottleSizesById,
      Map<String, RegionDocument> regionsById,
      Map<String, CountryDocument> countriesById
  ) {
    WineDocument wine = winesById.get(bottle.getWineId());
    ProducerDocument producer = wine == null ? null : producersById.get(wine.getProducerId());
    BottleSizeDocument bottleSize = bottleSizesById.get(bottle.getBottleSizeId());
    RegionDocument region = wine == null ? null : regionsById.get(wine.getRegionId());
    CountryDocument country = region == null ? null : countriesById.get(region.getCountryId());

    return new BottleListItemDto(
        bottle.getId(),
        valueOrFallback(wine == null ? null : wine.getName(), "Unknown wine"),
        valueOrFallback(producer == null ? null : producer.getName(), "Unknown producer"),
        vintageDisplay(bottle.getVintage()),
        valueOrFallback(wine == null ? null : wine.getWineType(), "Unknown"),
        valueOrFallback(bottleSize == null ? null : bottleSize.getLabel(), "Unknown"),
        valueOrFallback(country == null ? null : country.getName(), "Unknown"),
        valueOrFallback(region == null ? null : region.getName(), "Unknown"),
        bottle.getQuantity() == null ? 0 : bottle.getQuantity()
    );
  }

  private String valueOrFallback(String value, String fallback) {
    if (value == null || value.isBlank()) {
      return fallback;
    }
    return value;
  }

  private String vintageDisplay(Integer vintage) {
    if (vintage == null || vintage == NON_VINTAGE_SENTINEL) {
      return "NV";
    }
    return String.valueOf(vintage);
  }

  @SuppressWarnings("null")
  private Map<String, WineDocument> mapWinesById(Iterable<WineDocument> entities) {
    return streamOf(entities).collect(Collectors.toMap(WineDocument::getId, wine -> wine));
  }
  @SuppressWarnings("null")
  private Map<String, ProducerDocument> mapProducersById(Iterable<ProducerDocument> entities) {
    return streamOf(entities).collect(Collectors.toMap(ProducerDocument::getId, producer -> producer));
  }

  @SuppressWarnings("null")
  private Map<String, BottleSizeDocument> mapBottleSizesById(Iterable<BottleSizeDocument> entities) {
    return streamOf(entities).collect(Collectors.toMap(BottleSizeDocument::getId, size -> size));
  }

  @SuppressWarnings("null")
  private Map<String, RegionDocument> mapRegionsById(Iterable<RegionDocument> entities) {
    return streamOf(entities).collect(Collectors.toMap(RegionDocument::getId, region -> region));
  }

  @SuppressWarnings("null")
  private Map<String, CountryDocument> mapCountriesById(Iterable<CountryDocument> entities) {
    return streamOf(entities).collect(Collectors.toMap(CountryDocument::getId, country -> country));
  }

  private <T> java.util.stream.Stream<T> streamOf(Iterable<T> entities) {
    if (entities == null) {
      return java.util.stream.Stream.empty();
    }
    return java.util.stream.StreamSupport.stream(entities.spliterator(), false);
  }

}
