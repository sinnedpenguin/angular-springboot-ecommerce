package com.angularspringbootecommerce.backend.repository;

import com.angularspringbootecommerce.backend.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByAuthority(String authority);
}