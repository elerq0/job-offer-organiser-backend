package com.eleren.jobofferorganiser.repository;

import com.eleren.jobofferorganiser.model.SearchOptions;
import com.eleren.jobofferorganiser.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SearchOptionsRepository extends JpaRepository<SearchOptions, Long> {

    Iterable<SearchOptions> findAllByUser(User user);
}
