package com.winecellar.importer.application.imports;

import com.winecellar.importer.application.OwnerContextProvider;
import com.winecellar.importer.infrastructure.mongo.inventory.EventDocument;
import com.winecellar.importer.infrastructure.mongo.inventory.EventRepository;
import java.time.Instant;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ImportEventRecorder {

  private final EventRepository eventRepository;
  private final OwnerContextProvider ownerContextProvider;

  public ImportEventRecorder(EventRepository eventRepository, OwnerContextProvider ownerContextProvider) {
    this.eventRepository = eventRepository;
    this.ownerContextProvider = ownerContextProvider;
  }

  public String recordCsvImport(String cellarId, String fileName, int importedCount, int skippedCount) {
    EventDocument event = new EventDocument();
    event.setActorUserId(ownerContextProvider.ownerId());
    event.setEventType("CSV_IMPORT");
    event.setOccurredAt(Instant.now());
    event.setPrimaryTargetType("CELLAR");
    event.setPrimaryTargetId(cellarId);
    event.setMetadata(Map.of(
        "importedCount", importedCount,
        "skippedCount", skippedCount,
        "fileName", fileName == null ? "unknown.csv" : fileName
    ));

    return eventRepository.save(event).getId();
  }
}
