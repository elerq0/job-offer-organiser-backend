package com.eleren.jobofferorganiser.service;

import com.eleren.jobofferorganiser.model.*;
import com.eleren.jobofferorganiser.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchOptionsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SearchOptionsRepository searchOptionsRepository;

    @Autowired
    TechnologyRepository technologyRepository;

    @Autowired
    ExperienceLevelRepository experienceLevelRepository;

    @Autowired
    WebsiteRepository websiteRepository;

    public Iterable<SearchOptions> getAllForUser(User user) {
        return searchOptionsRepository.findAllByUser(user);
    }

    public long create(User user, String title, String location, List<String> technologies,
                       List<String> experienceLevels, List<String> websites) throws Exception {
        SearchOptions searchOptions = new SearchOptions(user, title, location);
        for (String technology : technologies)
            searchOptions.addTechnology(technologyRepository.findByName(technology)
                    .orElseThrow(() -> new Exception("Technology [" + technology + "] does not exist in the database")));

        for (String experienceLevel : experienceLevels)
            searchOptions.addExperienceLevel(experienceLevelRepository.findByName(experienceLevel)
                    .orElseThrow(() -> new Exception("Experience level [" + experienceLevel + "] does not exist in the database")));

        for (String website : websites)
            searchOptions.addWebsite(websiteRepository.findByName(website)
                    .orElseThrow(() -> new Exception("Website [" + website + "] does not exist in the database")));

        searchOptionsRepository.save(searchOptions);
        return searchOptions.getId();
    }

    public void delete(User user, long searchOptionsId) throws Exception {
        if (searchOptionsRepository.getOne(searchOptionsId).getUser().equals(user))
            searchOptionsRepository.deleteById(searchOptionsId);
        else
            throw new Exception("You do not have access to that search options!");
    }

    public SearchOptions get(User user, long searchOptionsId) throws Exception {
        SearchOptions searchOptions = searchOptionsRepository.getOne(searchOptionsId);
        if (searchOptions.getUser().equals(user))
            return searchOptions;
        else
            throw new Exception("You do not have access to that search options!");
    }

    public Iterable<Technology> getAvailableTechnologies() {
        return technologyRepository.findAllByOrderByName();
    }

    public Iterable<ExperienceLevel> getAvailableExperienceLevels() {
        return experienceLevelRepository.findAll();
    }

    public Iterable<Website> getAvailableWebsites() {
        return websiteRepository.findAll();
    }
}
