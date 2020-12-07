package com.eleren.jobofferorganiser.util;

import com.eleren.jobofferorganiser.dto.OfferSimpleDto;
import com.eleren.jobofferorganiser.dto.SearchOptionsDto;
import com.eleren.jobofferorganiser.model.ExperienceLevel;
import com.eleren.jobofferorganiser.model.ProgrammingLanguage;
import com.eleren.jobofferorganiser.model.SearchOptions;
import com.eleren.jobofferorganiser.model.Website;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("SpellCheckingInspection")
public class WebsitesViewerUtil {

    private SearchOptions searchOptions;
    private Environment env;

    public void setSearchOptions(SearchOptions searchOptions) {
        this.searchOptions = searchOptions;
    }

    public void setEnv(Environment env) throws Exception {
        this.env = env;

        if(Objects.isNull(env.getProperty("chromedriverpath")) || Objects.requireNonNull(env.getProperty("chromedriverpath")).isEmpty())
            throw new Exception("Environment property [chromedriverpath] needs to be set");
    }

    public List<OfferSimpleDto> getOffers() throws Exception {
        RequestExecutor requestExecutor = new RequestExecutor(env);

        List<SearchOptionsDto> searchOptionsDtoList =
                createSearchOptionsDtoList(
                        searchOptions.getTitle(),
                        searchOptions.getLocation(),
                        searchOptions.getProgrammingLanguages().stream().map(ProgrammingLanguage::getName).collect(Collectors.toList()),
                        searchOptions.getExperienceLevels().stream().map(ExperienceLevel::getName).collect(Collectors.toList()));

        List<WebsiteUtil> websiteUtilList =
                createWebsiteUtilList(
                        searchOptions.getWebsites().stream().map(Website::getName).collect(Collectors.toList()),
                        searchOptionsDtoList,
                        requestExecutor);

        List<OfferSimpleDto> offerSimpleDtoList = new ArrayList<>();
        List<Thread> threadList = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        for (WebsiteUtil websiteUtil : websiteUtilList) {
            Thread thread = new Thread(websiteUtil);
            threadList.add(thread);
            thread.start();
        }
        for (Thread thread : threadList)
            thread.join();

        for (WebsiteUtil websiteUtil : websiteUtilList) {
            try {
                offerSimpleDtoList.addAll(websiteUtil.getOffers());
            } catch (Exception e) {
                errors.add(websiteUtil.getClass().getSimpleName() + ": " + e.getMessage() + " : " + websiteUtil.buildUrl());
            }
        }

        requestExecutor.close();
        if(errors.size() > 0)
            throw new Exception(errors.toString());

        return offerSimpleDtoList;
    }

    private List<WebsiteUtil> createWebsiteUtilList(List<String> websites,
                                                    List<SearchOptionsDto> searchOptionsDtoList,
                                                    RequestExecutor requestExecutor) throws Exception {
        List<WebsiteUtil> websiteUtilList = new ArrayList<>();

        for (SearchOptionsDto searchOptionsDto : searchOptionsDtoList) {
            for (String website : websites) {
                WebsiteUtil websiteUtil;
                switch (website.toLowerCase()) {
                    case "pracuj.pl":
                        websiteUtil = new PracujUtil();
                        break;

                    case "nofluffjobs.com":
                        websiteUtil = new NoFluffJobsUtil();
                        break;

                    case "justjoin.it":
                        websiteUtil = new JustJoinItUtil();
                        break;

                    case "indeed.com":
                        websiteUtil = new IndeedUtil();
                        break;

                    default:
                        throw new Exception("Unknown website [" + website + "]");
                }

                websiteUtil.setSearchOptionsDto(searchOptionsDto);
                websiteUtil.setRequestExecutor(requestExecutor);

                if ((websiteUtil.getClass().equals(JustJoinItUtil.class)) &&
                        searchOptionsDto.getProgrammingLanguage().equals(""))
                    throw new Exception(website + " is IT website. You need to specify programming language");

                if (websiteUtil.getClass().equals(JustJoinItUtil.class) &&
                        searchOptionsDto.getExperienceLevel().toLowerCase().equals("trainee"))
                    continue; // on this page there is no way to search by expirence level: trainee

                if (websiteUtil.getClass().equals(JustJoinItUtil.class) &&
                        searchOptionsDto.getProgrammingLanguage().toLowerCase().equals("sql"))
                    continue; // no sql language on this page

                if (websiteUtil.getClass().equals(IndeedUtil.class) &&
                        searchOptionsDto.getExperienceLevel().toLowerCase().equals("trainee"))
                {
                    SearchOptionsDto searchOptionsDto_changed = new SearchOptionsDto();
                    searchOptionsDto_changed.setTitle(searchOptionsDto.getTitle());
                    searchOptionsDto_changed.setLocation(searchOptionsDto.getLocation());
                    searchOptionsDto_changed.setProgrammingLanguage(searchOptionsDto.getProgrammingLanguage());
                    searchOptionsDto_changed.setExperienceLevel("Praktykant");

                    websiteUtil.setSearchOptionsDto(searchOptionsDto_changed);
                }

                websiteUtilList.add(websiteUtil);
            }
        }

        return websiteUtilList;
    }

    private List<SearchOptionsDto> createSearchOptionsDtoList(
            String title, String location, List<String> programmingLanguages, List<String> experienceLevels) {
        List<SearchOptionsDto> searchOptionsDtoList = new ArrayList<>();

        for (String programmingLanguage : programmingLanguages) {
            searchOptionsDtoList.addAll(createSearchOptionsDtoList_ExperienceLevel(
                    title, location, programmingLanguage, experienceLevels));
        }

        if (programmingLanguages.size() == 0) {
            searchOptionsDtoList.addAll(createSearchOptionsDtoList_ExperienceLevel(
                    title, location, "", experienceLevels));
        }
        return searchOptionsDtoList;
    }

    private List<SearchOptionsDto> createSearchOptionsDtoList_ExperienceLevel(
            String title, String location, String programmingLanguage, List<String> experienceLevels) {
        List<SearchOptionsDto> searchOptionsDtoList = new ArrayList<>();

        for (String experienceLevel : experienceLevels) {
            SearchOptionsDto searchOptionsDto = new SearchOptionsDto();
            searchOptionsDto.setTitle(title);
            searchOptionsDto.setLocation(location);
            searchOptionsDto.setProgrammingLanguage(programmingLanguage);
            searchOptionsDto.setExperienceLevel(experienceLevel);

            searchOptionsDtoList.add(searchOptionsDto);
        }
        if (experienceLevels.size() == 0) {
            SearchOptionsDto searchOptionsDto = new SearchOptionsDto();
            searchOptionsDto.setTitle(title);
            searchOptionsDto.setLocation(location);
            searchOptionsDto.setProgrammingLanguage(programmingLanguage);
            searchOptionsDto.setExperienceLevel("");

            searchOptionsDtoList.add(searchOptionsDto);
        }
        return searchOptionsDtoList;
    }
}