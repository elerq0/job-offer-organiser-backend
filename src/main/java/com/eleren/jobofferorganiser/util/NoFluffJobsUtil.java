package com.eleren.jobofferorganiser.util;

import com.eleren.jobofferorganiser.dto.OfferSimpleDto;
import com.eleren.jobofferorganiser.dto.SearchOptionsDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class NoFluffJobsUtil implements WebsiteUtil {

    private final List<OfferSimpleDto> offerSimpleDtoList = new ArrayList<>();
    private Exception exception;
    private SearchOptionsDto searchOptionsDto;
    private RequestExecutor requestExecutor;

    @Override
    public void setSearchOptionsDto(SearchOptionsDto searchOptionsDto) {
        this.searchOptionsDto = searchOptionsDto;
    }

    @Override
    public void setRequestExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    @Override
    public void run() {
        try {
            String url = buildUrl();
            do {
                Document doc = Jsoup.parse(requestExecutor.execute(url), url);

                if (doc.getElementsByTag("nfj-no-offers-found-header").size() > 0)
                    return;

                url = "";
                Element next = doc.getElementsByClass("page-link").last();
                if (next != null && next.absUrl("href").contains("page="))
                    url = next.absUrl("href");

                for (Element offer : doc.select("a[class*=posting-list-item posting-list-item]")) {
                    OfferSimpleDto offerSimpleDto = new OfferSimpleDto();
                    offerSimpleDto.setOfferName(offer.select("div > h3").first().text()); // "h3[class*=posting-title]"
                    offerSimpleDto.setCompanyName(offer.select("div > span").first().text().trim()); // "span[class*=posting-title__company]"
                    offerSimpleDto.setOfferLink(offer.absUrl("href"));

                    offerSimpleDtoList.add(offerSimpleDto);
                }
            }
            while (!url.equals(""));

        } catch (Exception e) {
            exception = e;
        }
    }

    @Override
    public String buildUrl() {
        StringBuilder url = new StringBuilder();
        url.append("https://nofluffjobs.com/pl/jobs/");
        url.append(searchOptionsDto.getLocation()).append("/");
        url.append(searchOptionsDto.getTechnology()).append("?");
        url.append("criteria=");
        if (!searchOptionsDto.getExperienceLevel().equals("")) {
            url.append("seniority%3D");
            url.append(searchOptionsDto.getExperienceLevel().toLowerCase());
            url.append("%20%20");
        }
        url.append(searchOptionsDto.getTitle().replaceAll(" ", "%20"));

        return url.toString();
    }

    public List<OfferSimpleDto> getOffers() throws Exception {
        if (exception != null)
            throw exception;

        return offerSimpleDtoList;
    }
}
