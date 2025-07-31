package com.sorushi.invoice.management.tenant_management.config;

import com.sorushi.invoice.management.tenant_management.tenancy.TenantInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private final TenantInterceptor tenantInterceptor;

  public WebConfig(TenantInterceptor tenantInterceptor) {
    this.tenantInterceptor = tenantInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // Intercept all API request
    registry.addInterceptor(tenantInterceptor).addPathPatterns("/**");
  }
}
