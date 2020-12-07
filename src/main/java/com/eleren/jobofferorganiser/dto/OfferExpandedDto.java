package com.eleren.jobofferorganiser.dto;

import java.util.HashSet;
import java.util.Set;

public class OfferExpandedDto {

    private Long id;
    private String companyName;
    private String offerName;
    private Set<String> offerLink = new HashSet<>();
    private Boolean applied;
    private Boolean skipped;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public Set<String> getOfferLink() {
        return offerLink;
    }

    public void addOfferLink(String link){
        this.offerLink.add(link);
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
