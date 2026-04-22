package com.winecellar.importer.config;

import com.winecellar.importer.infrastructure.mongo.cellar.CellarDocument;
import com.winecellar.importer.infrastructure.mongo.cellar.CellarRepository;
import com.winecellar.importer.infrastructure.mongo.cellar.CompartmentDocument;
import com.winecellar.importer.infrastructure.mongo.cellar.CompartmentRepository;
import java.time.Instant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "winecellar.bootstrap", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DefaultCellarBootstrap implements ApplicationRunner {

  private final CellarRepository cellarRepository;
  private final CompartmentRepository compartmentRepository;

  public DefaultCellarBootstrap(CellarRepository cellarRepository, CompartmentRepository compartmentRepository) {
    this.cellarRepository = cellarRepository;
    this.compartmentRepository = compartmentRepository;
  }

  @Override
  public void run(ApplicationArguments args) {
    CellarDocument cellar = cellarRepository.findByName("Default Cellar")
        .orElseGet(() -> {
          CellarDocument created = new CellarDocument();
          created.setName("Default Cellar");
          created.setCreatedAt(Instant.now());
          return cellarRepository.save(created);
        });

    compartmentRepository.findFirstByCellarIdOrderByCreatedAtAsc(cellar.getId())
        .orElseGet(() -> {
          CompartmentDocument created = new CompartmentDocument();
          created.setCellarId(cellar.getId());
          created.setLabel("Default Compartment");
          created.setCreatedAt(Instant.now());
          return compartmentRepository.save(created);
        });
  }
}
