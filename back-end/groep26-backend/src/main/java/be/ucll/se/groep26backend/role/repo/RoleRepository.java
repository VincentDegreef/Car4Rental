package be.ucll.se.groep26backend.role.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import be.ucll.se.groep26backend.role.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}