package com.sorushi.invoice.management.tenant_management.repository;

import com.sorushi.invoice.management.tenant_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
