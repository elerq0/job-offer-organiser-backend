package com.eleren.jobofferorganiser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SearchOptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @NotNull
    @JsonIgnore
    private User user;

    @NotEmpty
    private String title;

    @NotEmpty
    private String location;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private final List<Technology> technologies = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    private final List<ExperienceLevel> experienceLevels = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @NotEmpty
    private final List<Website> websites = new ArrayList<>();

    @OneToMany(mappedBy = "searchOptions", cascade = CascadeType.ALL)
    @JsonIgnore
    private final List<SearchOptions_Offer> searchOptionsOffers = new ArrayList<>();

    public SearchOptions() {
    }

    public SearchOptions(User user, String title, String location) {
        this.user = user;
        this.title = title;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public List<Technology> getTechnologies() {
        return technologies;
    }

    public List<ExperienceLevel> getExperienceLevels() {
        return experienceLevels;
    }

    public List<Website> getWebsites() {
        return websites;
    }

    public List<SearchOptions_Offer> getSearchOptionsOffers() {
        return searchOptionsOffers;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void addTechnology(Technology language) {
        this.technologies.add(language);
    }

    public void addExperienceLevel(ExperienceLevel level) {
        this.experienceLevels.add(level);
    }

    public void addWebsite(Website website) {
        this.websites.add(website);
    }

    public void addSearchOptionsOffer(SearchOptions_Offer searchOptionsOffer) {
        this.searchOptionsOffers.add(searchOptionsOffer);
    }
}
