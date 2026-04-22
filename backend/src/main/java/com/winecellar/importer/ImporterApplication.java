package com.winecellar.importer;

import com.winecellar.importer.config.OwnerProperties;
import com.winecellar.importer.config.WebCorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({OwnerProperties.class, WebCorsProperties.class})
public class ImporterApplication {

  public static void main(String[] args) {
    SpringApplication.run(ImporterApplication.class, args);
  }
}
