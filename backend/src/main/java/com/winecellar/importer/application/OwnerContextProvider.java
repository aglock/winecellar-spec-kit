package com.winecellar.importer.application;

import com.winecellar.importer.config.OwnerProperties;
import org.springframework.stereotype.Component;

@Component
public class OwnerContextProvider {

  private final OwnerProperties ownerProperties;

  public OwnerContextProvider(OwnerProperties ownerProperties) {
    this.ownerProperties = ownerProperties;
  }

  public String ownerId() {
    return ownerProperties.getId();
  }

  public String ownerDisplayName() {
    return ownerProperties.getDisplayName();
  }
}
