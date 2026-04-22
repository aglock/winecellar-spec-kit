package com.winecellar.importer.infrastructure.mongo.inventory;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("wines")
public class WineDocument {

  @Id
  private String id;
  private String name;
  private String normalizedName;
  private String producerId;
  private String wineType;
  private String regionId;
  private String appellationId;
  private String varietal;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNormalizedName() {
    return normalizedName;
  }

  public void setNormalizedName(String normalizedName) {
    this.normalizedName = normalizedName;
  }

  public String getProducerId() {
    return producerId;
  }

  public void setProducerId(String producerId) {
    this.producerId = producerId;
  }

  public String getWineType() {
    return wineType;
  }

  public void setWineType(String wineType) {
    this.wineType = wineType;
  }

  public String getRegionId() {
    return regionId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }

  public String getAppellationId() {
    return appellationId;
  }

  public void setAppellationId(String appellationId) {
    this.appellationId = appellationId;
  }

  public String getVarietal() {
    return varietal;
  }

  public void setVarietal(String varietal) {
    this.varietal = varietal;
  }
}
