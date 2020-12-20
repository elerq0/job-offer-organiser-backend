package com.eleren.jobofferorganiser.controller;

import com.eleren.jobofferorganiser.model.ExperienceLevel;
import com.eleren.jobofferorganiser.model.Technology;
import com.eleren.jobofferorganiser.model.SearchOptions;
import com.eleren.jobofferorganiser.model.Website;
import com.eleren.jobofferorganiser.service.OfferService;
import com.eleren.jobofferorganiser.service.SearchOptionsService;
import com.eleren.jobofferorganiser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("search-options")
public class SearchOptionsController {

    @Autowired
    private UserService userService;

    @Autowired
    private SearchOptionsService searchOptionsService;

    @Autowired
    private OfferService offerService;

    @GetMapping(path = "/available-technologies")
    public ResponseEntity<Iterable<Technology>> getAvailableTechnologies() {
        return new ResponseEntity<>(searchOptionsService.getAvailableTechnologies(), HttpStatus.OK);
    }

    @GetMapping(path = "/available-experience-levels")
    public ResponseEntity<Iterable<ExperienceLevel>> getAvailableExperienceLevels() {
        return new ResponseEntity<>(searchOptionsService.getAvailableExperienceLevels(), HttpStatus.OK);
    }

    @GetMapping(path = "/available-websites")
    public ResponseEntity<Iterable<Website>> getAvailableWebsites() {
        return new ResponseEntity<>(searchOptionsService.getAvailableWebsites(), HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @PreAuthorize("isAuthenticated()")
    @PostMapping()
    public ResponseEntity<?> create(@RequestBody Map<String, Object> searchOptions) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            return new ResponseEntity<>(searchOptionsService.create(userService.getByUsername(username),
                    searchOptions.get("title").toString(),
                    searchOptions.get("location").toString(),
                    (List<String>) searchOptions.get("technologies"),
                    (List<String>) searchOptions.get("experienceLevels"),
                    (List<String>) searchOptions.get("websites")), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            searchOptionsService.delete(userService.getByUsername(username), id);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage().replaceAll(SearchOptions.class.getPackageName() + ".", ""), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping()
    public ResponseEntity<?> getAll() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return new ResponseEntity<>(searchOptionsService.getAllForUser(userService.getByUsername(username)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> get(@PathVariable long id) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return new ResponseEntity<>(searchOptionsService.get(userService.getByUsername(username), id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{id}/offers")
    public ResponseEntity<?> getOffers(@PathVariable long id) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return new ResponseEntity<>(offerService.getOffers(userService.getByUsername(username), id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/{id}/offers/{offerId}/applied")
    public ResponseEntity<?> setAppliedFlag(@PathVariable long id,
                                            @PathVariable long offerId,
                                            @RequestBody Map<String, Object> body) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            offerService.setAppliedFlag(userService.getByUsername(username),
                    id, offerId, (boolean) body.get("value"));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/{id}/offers/{offerId}/skipped")
    public ResponseEntity<?> setSkippedFlag(@PathVariable long id,
                                            @PathVariable long offerId,
                                            @RequestBody Map<String, Object> body) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            offerService.setSkippedFlag(userService.getByUsername(username),
                    id, offerId, (boolean) body.get("value"));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
