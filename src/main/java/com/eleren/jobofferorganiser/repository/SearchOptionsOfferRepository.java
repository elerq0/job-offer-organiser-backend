package com.eleren.jobofferorganiser.repository;

import com.eleren.jobofferorganiser.model.Offer;
import com.eleren.jobofferorganiser.model.SearchOptions;
import com.eleren.jobofferorganiser.model.SearchOptions_Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchOptionsOfferRepository extends JpaRepository<SearchOptions_Offer, Long> {

    Optional<SearchOptions_Offer> findBySearchOptionsAndOffer(SearchOptions searchOptions, Offer offer);
}
