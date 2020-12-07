package com.eleren.jobofferorganiser.repository;

import com.eleren.jobofferorganiser.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    Optional<Offer> findByNameIgnoreCaseAndCompanyIgnoreCase(String name, String company);

}