package com.eleren.jobofferorganiser.util;

import com.eleren.jobofferorganiser.dto.OfferSimpleDto;
import com.eleren.jobofferorganiser.dto.SearchOptionsDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class PracujUtil implements WebsiteUtil {

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
                Elements next = doc.getElementsByClass("pagination_element pagination_element--next");
                if (next.size() == 1)
                    url = next.get(0).getElementsByTag("a").first().absUrl("href");

                Elements elems = doc.getElementsByTag("script");
                Element e = elems.stream().filter(x -> x.html().startsWith("window.__INITIAL_STATE__ = ")).findFirst().get();
                String jsonStr = e.html().replaceFirst("window.__INITIAL_STATE__ = ", "");
                if (jsonStr.contains("\"isAppBannerModificator\":false}"))
                    jsonStr = jsonStr.substring(0, jsonStr.indexOf("\"isAppBannerModificator\":false}") + "\"isAppBannerModificator\":false}".length());
                else if (jsonStr.contains("\"isAppBannerModificator\":true}"))
                    jsonStr = jsonStr.substring(0, jsonStr.indexOf("\"isAppBannerModificator\":true}") + "\"isAppBannerModificator\":true}".length());
                else if (jsonStr.contains(";\r"))
                    jsonStr = jsonStr.substring(0, jsonStr.indexOf(";\r"));
                else
                    throw new Exception("Failed to process json");

                JSONArray offers = (JSONArray) ((JSONObject) JSONValue.parse(jsonStr)).get("offers");

                for (Object offer : offers) {
                    OfferSimpleDto offerSimpleDto = new OfferSimpleDto();
                    offerSimpleDto.setOfferName(((JSONObject) offer).get("jobTitle").toString());
                    offerSimpleDto.setCompanyName(((JSONObject) offer).get("employer").toString());
                    offerSimpleDto.setOfferLink(((JSONObject) ((JSONArray) ((JSONObject) offer).get("offers")).get(0)).get("offerUrl").toString());

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
        if (!searchOptionsDto.getTechnology().equals(""))
            titleStr.append(searchOptionsDto.getTechnology()).append(" ");
        titleStr.append(searchOptionsDto.getTitle());

        StringBuilder url = new StringBuilder();
        url.append("https://www.pracuj.pl/praca/");
        url.append(titleStr.toString().replaceAll(" ", "%20")).append(";kw/");
        url.append(searchOptionsDto.getLocation()).append(";wp?");
        url.append("rd=20");
        if (!searchOptionsDto.getExperienceLevel().equals("")) {
            url.append("&et=");
            switch (searchOptionsDto.getExperienceLevel().toLowerCase()) {
                case "trainee":
                    url.append(1);
                    break;
                case "junior":
                    url.append(17);
                    break;
                case "mid":
                    url.append(4);
                    break;
                case "senior":
                default:
                    url.append(18);
                    break;
            }
        }
        return url.toString();
    }

    public List<OfferSimpleDto> getOffers() throws Exception {
        if (exception != null)
            throw exception;

        return offerSimpleDtoList;
    }
}
