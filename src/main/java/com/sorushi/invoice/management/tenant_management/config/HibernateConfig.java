package com.sorushi.invoice.management.tenant_management.config;

import com.sorushi.invoice.management.tenant_management.tenancy.SchemaMultiTenantConnectionProvider;
import com.sorushi.invoice.management.tenant_management.tenancy.TenantIdentifierResolver;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.MultiTenancySettings;
import org.hibernate.cfg.SchemaToolingSettings;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

  private final DataSource dataSource;
  private final SchemaMultiTenantConnectionProvider connectionProvider;
  private final TenantIdentifierResolver tenantIdentifierResolver;

  public HibernateConfig(DataSource dataSource,
      SchemaMultiTenantConnectionProvider connectionProvider,
      TenantIdentifierResolver tenantIdentifierResolver) {
    this.dataSource = dataSource;
    this.connectionProvider = connectionProvider;
    this.tenantIdentifierResolver = tenantIdentifierResolver;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      EntityManagerFactoryBuilder builder) {
    Map<String, Object> hibernateProps = new HashMap<>();
    hibernateProps.put(JdbcSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
    hibernateProps.put(JdbcSettings.SHOW_SQL, true);
    hibernateProps.put(JdbcSettings.FORMAT_SQL, true);
    hibernateProps.put(SchemaToolingSettings.HBM2DDL_AUTO, "none");

    hibernateProps.put(MultiTenancySettings.TENANT_IDENTIFIER_TO_USE_FOR_ANY_KEY, "public");
    hibernateProps.put(MultiTenancySettings.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
    hibernateProps.put(MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER,
        tenantIdentifierResolver);

    return builder.dataSource(dataSource)
        .packages("com.sorushi.invoice.management.tenant_management.entity")
        .persistenceUnit("default").properties(hibernateProps).build();
  }

  @Bean
  public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }
}
