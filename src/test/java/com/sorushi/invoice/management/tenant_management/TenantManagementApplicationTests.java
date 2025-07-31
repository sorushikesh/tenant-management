package com.sorushi.invoice.management.tenant_management;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestTenantConfig.class)
@SpringBootTest
class TenantManagementApplicationTests {

	@Test
	void contextLoads() {
	}

}
