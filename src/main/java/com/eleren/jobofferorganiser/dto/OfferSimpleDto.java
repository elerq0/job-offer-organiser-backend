package com.eleren.jobofferorganiser.dto;

public class OfferSimpleDto {

    private String companyName;
    private String offerName;
    private String offerLink;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName.replaceAll("(?i)Sp. z o.o.", "").trim();
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName.trim();
    }

    public String getOfferLink() {
        return offerLink;
    }

    public void setOfferLink(String offerLink) {
        this.offerLink = offerLink;
    }
}
