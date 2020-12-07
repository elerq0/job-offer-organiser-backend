package com.eleren.jobofferorganiser.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SearchOptions_Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private SearchOptions searchOptions;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Offer offer;

    private LocalDateTime created;

    private Boolean applied;

    private Boolean skipped;

    public long getId() {
        return id;
    }

    public SearchOptions getSearchOptions() {
        return searchOptions;
    }

    public void setSearchOptions(SearchOptions searchOptions) {
        this.searchOptions = searchOptions;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Boolean getApplied() {
        return applied;
    }

    public void setApplied(Boolean applied) {
        this.applied = applied;
    }

    public Boolean getSkipped() {
        return skipped;
    }

    public void setSkipped(Boolean skipped) {
        this.skipped = skipped;
    }
}
