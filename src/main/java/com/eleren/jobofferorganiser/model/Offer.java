package com.eleren.jobofferorganiser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String company;
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
    private List<SearchOptions_Offer> searchOptionsOffers = new ArrayList<SearchOptions_Offer>();

    public long getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SearchOptions_Offer> getSearchOptionsOffers() {
        return searchOptionsOffers;
    }

    public void addSearchOptionsOffer(SearchOptions_Offer searchOptionsOffer){
        searchOptionsOffers.add(searchOptionsOffer);
    }
}
