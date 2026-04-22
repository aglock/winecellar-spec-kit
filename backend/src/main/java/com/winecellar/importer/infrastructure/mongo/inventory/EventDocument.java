package com.winecellar.importer.infrastructure.mongo.inventory;

import java.time.Instant;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("events")
public class EventDocument {

  @Id
  private String id;
  private String actorUserId;
  private String eventType;
  private Instant occurredAt;
  private String primaryTargetType;
  private String primaryTargetId;
  private Map<String, Object> metadata;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getActorUserId() {
    return actorUserId;
  }

  public void setActorUserId(String actorUserId) {
    this.actorUserId = actorUserId;
  }

  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public Instant getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(Instant occurredAt) {
    this.occurredAt = occurredAt;
  }

  public String getPrimaryTargetType() {
    return primaryTargetType;
  }

  public void setPrimaryTargetType(String primaryTargetType) {
    this.primaryTargetType = primaryTargetType;
  }

  public String getPrimaryTargetId() {
    return primaryTargetId;
  }

  public void setPrimaryTargetId(String primaryTargetId) {
    this.primaryTargetId = primaryTargetId;
  }

  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }
}
