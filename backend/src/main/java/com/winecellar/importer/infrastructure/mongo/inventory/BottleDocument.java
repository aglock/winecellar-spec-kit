package com.winecellar.importer.infrastructure.mongo.inventory;

import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("bottles")
public class BottleDocument {

  @Id
  private String id;
  private String cellarId;
  private String compartmentId;
  private String wineId;
  private String bottleSizeId;
  private Integer vintage;
  private Integer quantity;
  private Integer pending;
  private BigDecimal value;
  private BigDecimal price;
  private String currency;
  private Integer consumeFromYear;
  private Integer consumeUntilYear;
  private BigDecimal professionalScore;
  private BigDecimal communityScore;
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

  public String getCompartmentId() {
    return compartmentId;
  }

  public void setCompartmentId(String compartmentId) {
    this.compartmentId = compartmentId;
  }

  public String getWineId() {
    return wineId;
  }

  public void setWineId(String wineId) {
    this.wineId = wineId;
  }

  public String getBottleSizeId() {
    return bottleSizeId;
  }

  public void setBottleSizeId(String bottleSizeId) {
    this.bottleSizeId = bottleSizeId;
  }

  public Integer getVintage() {
    return vintage;
  }

  public void setVintage(Integer vintage) {
    this.vintage = vintage;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Integer getPending() {
    return pending;
  }

  public void setPending(Integer pending) {
    this.pending = pending;
  }

  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Integer getConsumeFromYear() {
    return consumeFromYear;
  }

  public void setConsumeFromYear(Integer consumeFromYear) {
    this.consumeFromYear = consumeFromYear;
  }

  public Integer getConsumeUntilYear() {
    return consumeUntilYear;
  }

  public void setConsumeUntilYear(Integer consumeUntilYear) {
    this.consumeUntilYear = consumeUntilYear;
  }

  public BigDecimal getProfessionalScore() {
    return professionalScore;
  }

  public void setProfessionalScore(BigDecimal professionalScore) {
    this.professionalScore = professionalScore;
  }

  public BigDecimal getCommunityScore() {
    return communityScore;
  }

  public void setCommunityScore(BigDecimal communityScore) {
    this.communityScore = communityScore;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}
