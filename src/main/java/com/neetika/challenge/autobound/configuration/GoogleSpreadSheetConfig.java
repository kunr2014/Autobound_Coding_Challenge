package com.neetika.challenge.autobound.configuration;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Configuration
public class GoogleSpreadSheetConfig {

    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

    @Bean
    public JsonFactory jsonFactory(){
        return JacksonFactory.getDefaultInstance();
    }

    @Bean
    public NetHttpTransport netHttpTransport() throws Exception{
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @Bean
    @Autowired
    public Credential getCredentials(GoogleClientSecrets clientSecrets, NetHttpTransport netHttpTransport, JsonFactory jsonFactory) throws IOException {
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                netHttpTransport, jsonFactory, clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    @Bean
    public GoogleClientSecrets googleClientSecrets(JsonFactory jsonFactory) throws IOException {
        // Load the client secret
        InputStream in = GoogleSpreadSheetConfig.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        return GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));
    }

    @Bean
    @Autowired
    public Sheets sheets (NetHttpTransport netHttpTransport, JsonFactory jsonFactory , Credential credential){
        return new Sheets.Builder(netHttpTransport, jsonFactory, credential).build();
    }
}
