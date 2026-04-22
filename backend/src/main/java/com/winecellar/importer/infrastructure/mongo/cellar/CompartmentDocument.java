package com.winecellar.importer.infrastructure.mongo.cellar;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("compartments")
public class CompartmentDocument {

  @Id
  private String id;
  private String cellarId;
  private String label;
  private Instant createdAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCellarId() {
    return cellarId;
  }

  public void setCellarId(String cellarId) {
    this.cellarId = cellarId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
