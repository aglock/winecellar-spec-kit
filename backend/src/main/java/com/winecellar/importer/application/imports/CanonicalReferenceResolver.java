package com.winecellar.importer.application.imports;

import com.winecellar.importer.infrastructure.mongo.reference.AppellationDocument;
import com.winecellar.importer.infrastructure.mongo.reference.AppellationRepository;
import com.winecellar.importer.infrastructure.mongo.reference.BottleSizeDocument;
import com.winecellar.importer.infrastructure.mongo.reference.BottleSizeRepository;
import com.winecellar.importer.infrastructure.mongo.reference.CountryDocument;
import com.winecellar.importer.infrastructure.mongo.reference.CountryRepository;
import com.winecellar.importer.infrastructure.mongo.reference.ProducerDocument;
import com.winecellar.importer.infrastructure.mongo.reference.ProducerRepository;
import com.winecellar.importer.infrastructure.mongo.reference.RegionDocument;
import com.winecellar.importer.infrastructure.mongo.reference.RegionRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class CanonicalReferenceResolver {

  private final ProducerRepository producerRepository;
  private final CountryRepository countryRepository;
  private final RegionRepository regionRepository;
  private final AppellationRepository appellationRepository;
  private final BottleSizeRepository bottleSizeRepository;

  public CanonicalReferenceResolver(
      ProducerRepository producerRepository,
      CountryRepository countryRepository,
      RegionRepository regionRepository,
      AppellationRepository appellationRepository,
      BottleSizeRepository bottleSizeRepository
  ) {
    this.producerRepository = producerRepository;
    this.countryRepository = countryRepository;
    this.regionRepository = regionRepository;
    this.appellationRepository = appellationRepository;
    this.bottleSizeRepository = bottleSizeRepository;
  }

  public ResolvedReferences resolve(
      String producerName,
      String countryName,
      String regionName,
      String appellationName,
      String bottleSizeLabel
  ) {
    ProducerDocument producer = resolveProducer(producerName);
    CountryDocument country = resolveCountry(countryName);
    RegionDocument region = resolveRegion(country, regionName);
    AppellationDocument appellation = resolveAppellation(region, appellationName);
    BottleSizeDocument bottleSize = resolveBottleSize(bottleSizeLabel);

    return new ResolvedReferences(
        producer.getId(),
        country.getId(),
        region.getId(),
        appellation == null ? null : appellation.getId(),
        bottleSize.getId()
    );
  }

  private ProducerDocument resolveProducer(String producerName) {
    String normalized = CsvValueNormalizer.normalizeKey(producerName);
    return producerRepository.findByNormalizedName(normalized)
        .orElseGet(() -> {
          ProducerDocument created = new ProducerDocument();
          created.setName(CsvValueNormalizer.normalizeText(producerName));
          created.setNormalizedName(normalized);
          return producerRepository.save(created);
        });
  }

  private CountryDocument resolveCountry(String countryName) {
    String normalized = CsvValueNormalizer.normalizeKey(countryName);
    return countryRepository.findByNormalizedName(normalized)
        .orElseGet(() -> {
          CountryDocument created = new CountryDocument();
          created.setName(CsvValueNormalizer.normalizeText(countryName));
          created.setNormalizedName(normalized);
          return countryRepository.save(created);
        });
  }

  private RegionDocument resolveRegion(CountryDocument country, String regionName) {
    String normalized = CsvValueNormalizer.normalizeKey(regionName);
    return regionRepository.findByCountryIdAndNormalizedName(country.getId(), normalized)
        .orElseGet(() -> {
          RegionDocument created = new RegionDocument();
          created.setCountryId(country.getId());
          created.setName(CsvValueNormalizer.normalizeText(regionName));
          created.setNormalizedName(normalized);
          return regionRepository.save(created);
        });
  }

  private AppellationDocument resolveAppellation(RegionDocument region, String appellationName) {
    if (CsvValueNormalizer.isUnknownOrBlank(appellationName)) {
      return null;
    }

    String normalized = CsvValueNormalizer.normalizeKey(appellationName);
    return appellationRepository.findByRegionIdAndNormalizedName(region.getId(), normalized)
        .orElseGet(() -> {
          AppellationDocument created = new AppellationDocument();
          created.setRegionId(region.getId());
          created.setName(CsvValueNormalizer.normalizeText(appellationName));
          created.setNormalizedName(normalized);
          return appellationRepository.save(created);
        });
  }

  private BottleSizeDocument resolveBottleSize(String bottleSizeLabel) {
    String normalized = CsvValueNormalizer.normalizeKey(bottleSizeLabel);
    return bottleSizeRepository.findByNormalizedLabel(normalized)
        .orElseGet(() -> {
          BottleSizeDocument created = new BottleSizeDocument();
          created.setLabel(CsvValueNormalizer.normalizeText(bottleSizeLabel));
          created.setNormalizedLabel(normalized);
          created.setVolumeMl(parseVolumeMl(bottleSizeLabel));
          return bottleSizeRepository.save(created);
        });
  }

  private Integer parseVolumeMl(String label) {
    String normalized = CsvValueNormalizer.normalizeText(label).toLowerCase(Locale.ROOT);
    if (normalized.endsWith("ml")) {
      String value = normalized.substring(0, normalized.length() - 2).trim();
      return Integer.parseInt(value);
    }
    if (normalized.endsWith("l")) {
      String value = normalized.substring(0, normalized.length() - 1).trim();
      BigDecimal liters = new BigDecimal(value);
      return liters.multiply(BigDecimal.valueOf(1000))
          .setScale(0, RoundingMode.HALF_UP)
          .intValue();
    }
    return null;
  }

  public record ResolvedReferences(
      String producerId,
      String countryId,
      String regionId,
      String appellationId,
      String bottleSizeId
  ) {
  }
}
