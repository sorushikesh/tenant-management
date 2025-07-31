package com.sorushi.invoice.management.tenant_management;

import com.sorushi.invoice.management.tenant_management.tenancy.SchemaMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@TestConfiguration
public class TestTenantConfig {

  @Bean
  public MultiTenantConnectionProvider<String> multiTenantConnectionProvider() {
    return new SimpleMultiTenantConnectionProvider();
  }

  public static class SimpleMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String> {
    private final DataSource dataSource;

    public SimpleMultiTenantConnectionProvider() {
      DriverManagerDataSource ds = new DriverManagerDataSource();
      ds.setDriverClassName("org.postgresql.Driver");
      ds.setUrl("jdbc:postgresql://localhost:5432/yourdb"); // real DB
      ds.setUsername("postgres");
      ds.setPassword("password");
      this.dataSource = ds;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
      return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
      connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
      return dataSource.getConnection(); // ignore tenant
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
      connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
      return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
      return unwrapType.isAssignableFrom(SchemaMultiTenantConnectionProvider.class);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
      if (unwrapType.isAssignableFrom(SchemaMultiTenantConnectionProvider.class)) {
        return (T) this;
      }
      throw new IllegalArgumentException("Unknown unwrap type: " + unwrapType);
    }
  }
}

