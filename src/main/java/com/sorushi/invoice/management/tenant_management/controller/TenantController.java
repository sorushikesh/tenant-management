package com.sorushi.invoice.management.tenant_management.controller;

import com.sorushi.invoice.management.tenant_management.util.FlywaySchemaCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tenant")
public class TenantController {

  private final FlywaySchemaCreator flywaySchemaCreator;

  public TenantController(FlywaySchemaCreator flywaySchemaCreator) {
    this.flywaySchemaCreator = flywaySchemaCreator;
  }

  @PostMapping("/admin/tenants")
  public ResponseEntity<String> createTenant(@RequestParam String tenantId) {
    flywaySchemaCreator.createAndMigrateTenantSchema(tenantId);
    return ResponseEntity.ok("Tenant '" + tenantId + "' created successfully.");
  }
}
