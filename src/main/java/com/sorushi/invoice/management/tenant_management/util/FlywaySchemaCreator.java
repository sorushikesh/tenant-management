package com.sorushi.invoice.management.tenant_management.util;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Slf4j
@Component
public class FlywaySchemaCreator {

  private final DataSource dataSource;

  public FlywaySchemaCreator(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void createAndMigrateTenantSchema(String tenantSchemaName) {

    try (Connection connection = dataSource.getConnection()) {

      Statement statement = connection.createStatement();

      // 1. Create schema if not exists
      statement.execute("CREATE SCHEMA IF NOT EXISTS " + tenantSchemaName);

      // 2. Run Flyway for the schema
      Flyway flyway = Flyway.configure().dataSource(dataSource).schemas(tenantSchemaName)
          .locations("classpath:db/migration").baselineOnMigrate(true).load();

      flyway.migrate();

      log.info("Schema {} created and migrated", tenantSchemaName);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create and migrate schema: " + tenantSchemaName);
    }
  }
}
