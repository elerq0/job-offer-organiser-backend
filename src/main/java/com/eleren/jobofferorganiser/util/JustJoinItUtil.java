package com.eleren.jobofferorganiser.util;

import com.eleren.jobofferorganiser.dto.OfferSimpleDto;
import com.eleren.jobofferorganiser.dto.SearchOptionsDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class JustJoinItUtil implements WebsiteUtil {

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
            Document doc = Jsoup.parse(requestExecutor.execute(url), url);

            Element html = doc.getElementsByClass("css-110u7ph").first();
            Elements listOfElements = html.select("a[class='css-18rtd1e'], div[class='css-1h51gp0']");
            for (Element elem : listOfElements) {
                if (elem.html().contains("</span> in other cities</span>"))
                    break;

                OfferSimpleDto offerSimpleDto = new OfferSimpleDto();
                offerSimpleDto.setCompanyName(elem.getElementsByClass("css-ajz12e").first().text());
                offerSimpleDto.setOfferName(elem.getElementsByClass("css-1x9zltl").first().text());
                offerSimpleDto.setOfferLink(elem.absUrl("href"));

                offerSimpleDtoList.add(offerSimpleDto);
            }
        } catch (Exception e) {
            exception = e;
        }
    }

    @Override
    public String buildUrl() {
        StringBuilder url = new StringBuilder();
        url.append("https://justjoin.it/");
        url.append(searchOptionsDto.getLocation().toLowerCase());
        url.append("/").append(searchOptionsDto.getProgrammingLanguage().toLowerCase());
        if (!searchOptionsDto.getExperienceLevel().equals(""))
            url.append("/").append(searchOptionsDto.getExperienceLevel().toLowerCase());

        return url.toString();
    }

    public List<OfferSimpleDto> getOffers() throws Exception {
        if (exception != null)
            throw exception;

        return offerSimpleDtoList;
    }
}
