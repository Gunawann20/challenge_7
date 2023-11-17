package org.binaracademy.challenge_7.repository;

import org.binaracademy.challenge_7.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findRolesById(Integer id);
}
