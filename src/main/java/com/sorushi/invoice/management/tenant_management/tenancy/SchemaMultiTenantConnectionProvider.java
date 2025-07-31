package com.sorushi.invoice.management.tenant_management.tenancy;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Component
public class SchemaMultiTenantConnectionProvider implements MultiTenantConnectionProvider<String> {

  private final DataSource dataSource;

  public SchemaMultiTenantConnectionProvider(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Connection getAnyConnection() throws SQLException {
    return dataSource.getConnection();
  }

  @Override
  public Connection getConnection(String tenantIdentifier) throws SQLException {
    final Connection connection = getAnyConnection();
    try {
      connection.setSchema(tenantIdentifier);
    } catch (SQLException e) {
      connection.close();
      throw new SQLException("Tenant schema not found: " + tenantIdentifier, e);
    }
    return connection;
  }

  @Override
  public void releaseAnyConnection(Connection connection) throws SQLException {
    connection.close();
  }

  @Override
  public void releaseConnection(String tenantIdentifier, Connection connection)
      throws SQLException {
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
