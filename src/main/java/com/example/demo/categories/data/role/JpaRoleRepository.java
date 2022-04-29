package com.example.demo.categories.data.role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleRepository extends JpaRepository<RoleDataMapper, Long> {
    RoleDataMapper findByRole(ERoles role);
}
