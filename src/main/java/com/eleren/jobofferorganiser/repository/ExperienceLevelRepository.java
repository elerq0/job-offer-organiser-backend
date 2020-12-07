package com.eleren.jobofferorganiser.repository;

import com.eleren.jobofferorganiser.model.ExperienceLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExperienceLevelRepository extends JpaRepository<ExperienceLevel, Long> {

    Optional<ExperienceLevel> findByName(String name);

}
