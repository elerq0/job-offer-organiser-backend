package com.eleren.jobofferorganiser.repository;

import com.eleren.jobofferorganiser.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechnologyRepository extends JpaRepository<Technology, Long> {

    Iterable<Technology> findAllByOrderByName();

    Optional<Technology> findByName(String name);
}
