package com.neetika.challenge.autobound.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.neetika.challenge.autobound.dto.NewsAPIResponse;
import com.neetika.challenge.autobound.entity.Company;
import com.neetika.challenge.autobound.entity.SpreadSheet;
import com.neetika.challenge.autobound.repository.SpreadSheetRepository;
import com.neetika.challenge.autobound.util.SafeCellValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class SpreadsheetService {

    private static final List<List<Object>> HEADERS = Arrays.asList(
            Arrays.asList(
                    "Company",
                    "Source",
                    "Author",
                    "Title",
                    "Description",
                    "URL",
                    "URLToImage",
                    "PublishedAt",
                    "Contents"));

    private static final Integer DAYS_IN_YEAR = 365;

    private Sheets sheets;
    private SpreadSheetRepository spreadSheetRepository;

    @Autowired
    public SpreadsheetService(Sheets sheets, SpreadSheetRepository spreadSheetRepository) {
        this.sheets = sheets;
        this.spreadSheetRepository = spreadSheetRepository;
    }

    public String getOrCreateNewSheet() {
        Optional<SpreadSheet> sheetIdOptional = spreadSheetRepository.findAll().stream().findFirst();
        if (!sheetIdOptional.isPresent()) {
            log.info("Sheet does not exist creating a new one...");
            try {
                Spreadsheet spreadSheet = new Spreadsheet().setProperties(new SpreadsheetProperties().setTitle("Autobound - Neetika Challenge"));
                spreadSheet = sheets.spreadsheets().create(spreadSheet).setFields("spreadsheetId").execute();

                spreadSheetRepository.save(SpreadSheet.builder().id(spreadSheet.getSpreadsheetId()).build());
                log.info("New Spreadsheet created and saved to DB, Id: {}", spreadSheet.getSpreadsheetId());

                return spreadSheet.getSpreadsheetId();
            } catch (IOException e) {
                throw new RuntimeException("Error while creating Spreadsheet", e);
            }
        }

        log.info("SpreadSheet Id: {}", sheetIdOptional.get().getId());
        return sheetIdOptional.get().getId();
    }


    public void writeToSheet(Company company, NewsAPIResponse response) {
        String spreadsheetId = getOrCreateNewSheet();
        log.info("Going to write articles to sheet {} ", company.getName());
        LocalDateTime currentDateTime = LocalDateTime.now();
        try {

            createCompanySpecificSheet(spreadsheetId, company.getName());
            clearSheetContents(spreadsheetId, company.getName());

            final AtomicInteger rowCounter = new AtomicInteger(1);
            List<ValueRange> data = new ArrayList<>();

            // Sheet 1 Summary
            data.add(new ValueRange().setRange("A1").setValues(Arrays.asList(Arrays.asList("Last Updated", SafeCellValue.get(new Date().toString())))));
            data.add(new ValueRange().setRange("A2").setValues(Arrays.asList(Arrays.asList("For Articles please refer to individual tab for each Company"))));


            // Company specific Sheets
            String rangePrefix = company.getName() + "!A";
            data.add(new ValueRange().setRange(rangePrefix + rowCounter.getAndIncrement()).setValues(HEADERS));
            response.getArticles().stream().filter(articleToFilter -> {
                // we will discard any article older than a year
                LocalDateTime publishedDate = LocalDateTime.ofInstant(articleToFilter.getPublishedAt().toInstant(), ZoneId.systemDefault());
                Long daysDifference = ChronoUnit.DAYS.between(publishedDate, currentDateTime);
                log.debug("Days difference: {} ", daysDifference);
                return daysDifference < DAYS_IN_YEAR;
            }).forEach(article -> {
                List<List<Object>> articleRow = Arrays.asList(
                        Arrays.asList(company.getName(),
                                SafeCellValue.get(article.getSource().getName()),
                                SafeCellValue.get(article.getAuthor()),
                                SafeCellValue.get(article.getTitle()),
                                SafeCellValue.get(article.getDescription()),
                                SafeCellValue.get(article.getUrl()),
                                SafeCellValue.get(article.getUrlToImage()),
                                SafeCellValue.get(article.getPublishedAt().toString()),
                                SafeCellValue.get(article.getContent())));
                data.add(new ValueRange().setRange(rangePrefix + rowCounter.getAndIncrement()).setValues(articleRow));
            });


            BatchUpdateValuesRequest batchBody = new BatchUpdateValuesRequest().setValueInputOption("USER_ENTERED").setData(data);
            sheets.spreadsheets().values().batchUpdate(spreadsheetId, batchBody).execute();
            log.info("SpreadSheet data written for Company: {}", company.getName());
        } catch (IOException e) {
            throw new RuntimeException("Problem writing to Spreadsheet", e);
        }
    }


    private void createCompanySpecificSheet(String spreadsheetId, String spreadsheetName) throws IOException {
        try {
            AddSheetRequest addSheetRequest = new AddSheetRequest();
            SheetProperties sheetProperties = new SheetProperties();
            sheetProperties.setTitle(spreadsheetName);
            addSheetRequest.setProperties(sheetProperties);
            BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
            Request request = new Request();
            request.setAddSheet(addSheetRequest);
            List<Request> allRequests = new ArrayList<>();
            allRequests.add(request);
            batchUpdateSpreadsheetRequest.setRequests(allRequests);
            sheets.spreadsheets().batchUpdate(spreadsheetId, batchUpdateSpreadsheetRequest).execute();
        } catch (Exception e) {
            // TODO find a better way to check sheet exists
            if (!e.getMessage().contains("already exists")) {
                throw new RuntimeException(e);
            }
        }


    }

    private void clearSheetContents(String spreadsheetId, String spreadSheetName) {
        try {
            ClearValuesRequest requestBody = new ClearValuesRequest();
            // TODO improve range later when data grows
            String range = spreadSheetName + "!A1:Z1000";

            sheets.spreadsheets().values().clear(spreadsheetId, range, requestBody).execute();
            log.info("All existing contents erased");
        } catch (Exception e) {
            log.warn("{}", e);
        }

    }

}
