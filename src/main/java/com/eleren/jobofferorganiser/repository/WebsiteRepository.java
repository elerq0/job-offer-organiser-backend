package com.eleren.jobofferorganiser.repository;

import com.eleren.jobofferorganiser.model.Website;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WebsiteRepository extends JpaRepository<Website, Long> {

    Optional<Website> findByName(String name);

}
