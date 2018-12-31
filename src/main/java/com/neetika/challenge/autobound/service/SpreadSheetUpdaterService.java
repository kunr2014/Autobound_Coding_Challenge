package com.neetika.challenge.autobound.service;

import com.neetika.challenge.autobound.dto.NewsAPIResponse;
import com.neetika.challenge.autobound.entity.Company;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SpreadSheetUpdaterService {

    private GoogleNewsFetcherService newsFetcherService;
    private CompanyService companyService;
    private SpreadsheetService spreadsheetService;

    @Autowired
    public SpreadSheetUpdaterService(SpreadsheetService spreadsheetService, GoogleNewsFetcherService newsFetcherService, CompanyService companyService) {
        this.newsFetcherService = newsFetcherService;
        this.companyService = companyService;
        this.spreadsheetService = spreadsheetService;
    }

    @Scheduled(cron = "0 0/30 * * * ?")
    public void fetchAndUpdate() {
        log.info("Triggered scheduled Cron Job at {}", new Date());
        List<Company> configuredCompanies = companyService.getAllConfiguredCompanies();

        configuredCompanies.forEach(company -> {
            log.info("Processing News for Company: {}", company);
            NewsAPIResponse response = newsFetcherService.fetchArticles(company.getName());
            log.info("Received Response: {}", response);
            spreadsheetService.writeToSheet(company, response);
        });

        log.info("Please verify results at https://docs.google.com/spreadsheets/d/{}/edit", spreadsheetService.getOrCreateNewSheet());
    }

}
