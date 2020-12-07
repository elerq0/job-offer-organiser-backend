package com.eleren.jobofferorganiser.util;

import com.eleren.jobofferorganiser.dto.OfferSimpleDto;
import com.eleren.jobofferorganiser.dto.SearchOptionsDto;

import java.util.List;

public interface WebsiteUtil extends Runnable {

    void setSearchOptionsDto(SearchOptionsDto searchOptionsDto);
    void setRequestExecutor(RequestExecutor requestExecutor);
    List<OfferSimpleDto> getOffers() throws Exception;
    String buildUrl();

}
