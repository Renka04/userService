package org.example.user.Repo;

import org.example.user.Entity.SoftSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SoftSkillRepository extends JpaRepository<SoftSkill, Long> {
    Optional<SoftSkill> findByName(String name);
}


