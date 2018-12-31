package com.neetika.challenge.autobound;


import com.neetika.challenge.autobound.service.SpreadSheetUpdaterService;
import com.neetika.challenge.autobound.service.SpreadsheetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableScheduling
@SpringBootApplication
public class NewsFeedAppMain {

    public static void main(String[] args) {
        SpringApplication.run(NewsFeedAppMain.class, args);
    }


    @Bean
    CommandLineRunner initialize(SpreadsheetService spreadsheetService,
                                 SpreadSheetUpdaterService spreadSheetUpdaterService) {
        log.info("Initializing Spreadsheet...");
        return args -> {
            spreadsheetService.getOrCreateNewSheet();

            //explicit call on start up to prepare contents
            spreadSheetUpdaterService.fetchAndUpdate();
        };
    }

}
