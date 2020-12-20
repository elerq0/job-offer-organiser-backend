package com.eleren.jobofferorganiser.util;

import com.eleren.jobofferorganiser.dto.OfferSimpleDto;
import com.eleren.jobofferorganiser.dto.SearchOptionsDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class IndeedUtil implements WebsiteUtil {

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

                if (doc.getElementsByClass("results-header__no-offers").size() > 0)
                    return;

                url = "";
                Elements links = doc.select("link[rel='next']");
                if (links.size() == 1)
                    url = links.get(0).absUrl("href");

                for (Element elem : doc.select("div[class*='jobsearch-SerpJobCard unifiedRow row result']")) {
                    OfferSimpleDto offerSimpleDto = new OfferSimpleDto();
                    if (elem.select("span.company").size() > 0)
                        offerSimpleDto.setCompanyName(elem.select("span.company").first().text());
                    else
                        offerSimpleDto.setCompanyName("NO_COMPANY");
                    offerSimpleDto.setOfferName(elem.select("h2.title > a").first().attr("title"));
                    offerSimpleDto.setOfferLink(elem.select("h2.title > a").first().absUrl("href"));

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
        StringBuilder titleStr = new StringBuilder();
        if (!searchOptionsDto.getExperienceLevel().equals(""))
            titleStr.append(searchOptionsDto.getExperienceLevel()).append(" ");
        if (!searchOptionsDto.getTechnology().equals(""))
            titleStr.append(searchOptionsDto.getTechnology()).append(" ");
        titleStr.append(searchOptionsDto.getTitle());

        return "https://pl.indeed.com/praca?q=" +
                titleStr.toString().replaceAll(" ", "+") +
                "&l=" + searchOptionsDto.getLocation() +
                "&limit=50";
    }

    public List<OfferSimpleDto> getOffers() throws Exception {
        if (exception != null)
            throw exception;

        return offerSimpleDtoList;
    }

}
