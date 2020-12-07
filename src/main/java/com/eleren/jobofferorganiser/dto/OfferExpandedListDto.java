package com.eleren.jobofferorganiser.dto;

import com.eleren.jobofferorganiser.model.Offer;

import java.util.ArrayList;
import java.util.Comparator;

public class OfferExpandedListDto extends ArrayList<OfferExpandedDto> {

    public boolean containsOffer(Offer offer){
        return this.stream().anyMatch(t -> t.getOfferName().equals(offer.getName()) &&
                t.getCompanyName().equals(offer.getCompany()));
    }

    public OfferExpandedDto getCorresponding(Offer offer){
        return this.stream().filter(t -> t.getOfferName().equals(offer.getName()) &&
                t.getCompanyName().equals(offer.getCompany())).findFirst().get();
    }

    public void sortByCompanyThenName(){
        this.sort(Comparator.comparing(OfferExpandedDto::getCompanyName)
                .thenComparing(OfferExpandedDto::getOfferName));
    }

}
