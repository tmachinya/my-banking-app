package com.example.bankingapp.repository;

import com.example.bankingapp.entity.Role;
import com.example.bankingapp.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
