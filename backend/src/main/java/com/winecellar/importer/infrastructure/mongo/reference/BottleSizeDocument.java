package com.winecellar.importer.infrastructure.mongo.reference;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("bottle_sizes")
public class BottleSizeDocument {

  @Id
  private String id;
  private String label;
  private String normalizedLabel;
  private Integer volumeMl;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getNormalizedLabel() {
    return normalizedLabel;
  }

  public void setNormalizedLabel(String normalizedLabel) {
    this.normalizedLabel = normalizedLabel;
  }

  public Integer getVolumeMl() {
    return volumeMl;
  }

  public void setVolumeMl(Integer volumeMl) {
    this.volumeMl = volumeMl;
  }
}
