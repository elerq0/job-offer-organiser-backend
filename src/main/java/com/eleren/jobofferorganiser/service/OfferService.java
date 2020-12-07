package com.eleren.jobofferorganiser.service;

import com.eleren.jobofferorganiser.dto.*;
import com.eleren.jobofferorganiser.model.Offer;
import com.eleren.jobofferorganiser.model.SearchOptions;
import com.eleren.jobofferorganiser.model.SearchOptions_Offer;
import com.eleren.jobofferorganiser.model.User;
import com.eleren.jobofferorganiser.repository.OfferRepository;
import com.eleren.jobofferorganiser.repository.SearchOptionsOfferRepository;
import com.eleren.jobofferorganiser.repository.SearchOptionsRepository;
import com.eleren.jobofferorganiser.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private SearchOptionsRepository searchOptionsRepository;

    @Autowired
    private SearchOptionsOfferRepository searchOptionsOfferRepository;

    @Autowired
    private Environment env;

    public List<OfferExpandedDto> getOffers(User user, long searchOptionsId) throws Exception {
        SearchOptions searchOptions = searchOptionsRepository.getOne(searchOptionsId);
        if (!searchOptions.getUser().equals(user))
            throw new Exception("You do not have access to that");

        WebsitesViewerUtil websitesViewer = new WebsitesViewerUtil();
        websitesViewer.setEnv(env);
        websitesViewer.setSearchOptions(searchOptions);
        List<OfferSimpleDto> offerSimpleDtoList = websitesViewer.getOffers();
        OfferExpandedListDto offerExpandedListDto = new OfferExpandedListDto();

        for (OfferSimpleDto offerSimpleDto : offerSimpleDtoList) {
            Offer offer;
            if(offerRepository.findByNameIgnoreCaseAndCompanyIgnoreCase(offerSimpleDto.getOfferName(), offerSimpleDto.getCompanyName()).isPresent()){
                offer = offerRepository.findByNameIgnoreCaseAndCompanyIgnoreCase(offerSimpleDto.getOfferName(), offerSimpleDto.getCompanyName()).get();
            } else {
                offer = new Offer();
                offer.setName(offerSimpleDto.getOfferName());
                offer.setCompany(offerSimpleDto.getCompanyName());

                offerRepository.save(offer);
            }

            SearchOptions_Offer searchOptionsOffer;
            if (searchOptionsOfferRepository.findBySearchOptionsAndOffer(searchOptions, offer).isPresent()) {
                searchOptionsOffer = searchOptionsOfferRepository.findBySearchOptionsAndOffer(searchOptions, offer).get();
            } else {
                searchOptionsOffer = new SearchOptions_Offer();
                searchOptionsOffer.setSearchOptions(searchOptions);
                searchOptionsOffer.setOffer(offer);
                searchOptionsOffer.setCreated(LocalDateTime.now());
                searchOptionsOffer.setApplied(false);
                searchOptionsOffer.setSkipped(false);

                searchOptionsOfferRepository.save(searchOptionsOffer);
            }

            OfferExpandedDto offerExpandedDto;
            if (offerExpandedListDto.containsOffer(offer)) {
                offerExpandedDto = offerExpandedListDto.getCorresponding(offer);
                offerExpandedDto.addOfferLink(offerSimpleDto.getOfferLink());
            } else {
                offerExpandedDto = new OfferExpandedDto();
                offerExpandedDto.setId(offer.getId());
                offerExpandedDto.setOfferName(offer.getName());
                offerExpandedDto.setCompanyName(offer.getCompany());
                offerExpandedDto.addOfferLink(offerSimpleDto.getOfferLink());
                offerExpandedDto.setApplied(searchOptionsOffer.getApplied());
                offerExpandedDto.setSkipped(searchOptionsOffer.getSkipped());

                offerExpandedListDto.add(offerExpandedDto);
            }
        }

        offerExpandedListDto.sortByCompanyThenName();
        return offerExpandedListDto;
    }

    public void setFlags(User user, long searchOptionsId, long offerId, boolean applied, boolean skipped) throws Exception {
        SearchOptions searchOptions = searchOptionsRepository.getOne(searchOptionsId);
        if (!searchOptions.getUser().equals(user))
            throw new Exception("You do not have access to that");

        Offer offer = offerRepository.getOne(offerId);
        if (!searchOptionsOfferRepository.findBySearchOptionsAndOffer(searchOptions, offer).isPresent())
            throw new Exception("Wrong offer entity");

        SearchOptions_Offer searchOptionsOffer = searchOptionsOfferRepository.findBySearchOptionsAndOffer(searchOptions, offer).get();
        searchOptionsOffer.setApplied(applied);
        searchOptionsOffer.setSkipped(skipped);

        searchOptionsOfferRepository.save(searchOptionsOffer);
    }
}