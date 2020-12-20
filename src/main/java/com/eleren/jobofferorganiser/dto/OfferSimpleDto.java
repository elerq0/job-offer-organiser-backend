package com.eleren.jobofferorganiser.dto;

public class OfferSimpleDto {

    private String companyName;
    private String offerName;
    private String offerLink;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        if (companyName.toLowerCase().replaceAll("\\s+", "").contains("zo.o.")) {
            this.companyName = companyName
                    .replaceAll("(?i)Sp.z o.o", "")
                    .replaceAll("(?i)Sp. z o.o.", "")
                    .replaceAll("(?i)Sp. z o. o.", "")
                    .replaceAll("(?i)Spółka z o.o.", "")
                    .replaceAll("(?i)SP Z O O", "")
                    .replaceAll("(?i)z o.o.", "")
                    .trim();
        } else {
            this.companyName = companyName.trim();
        }
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
