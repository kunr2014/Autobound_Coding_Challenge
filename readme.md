# google-newsfeed-app

This app uses Google News API to generate a constantly updating Google Spreadsheet on company-specific articles.

Application uses file based H2 database to store all the companies that are configured for articles.

On start up app creates a new Sheet and stores the sheet Id for any subsequent requests.

Once news are fetched from newsapi.org they are written to the google spreadsheet in a dedicated tab.

### View all Companies(Can be viewed in Browser)
GET http://localhost:8080/companies

### Add a new Company (Use REST client)
POST http://localhost:8080/companies/{name}

### Delete existing Company(Use REST Client)
DELETE http://localhost:8080/companies/{name}
 
 
## Running Application
```java
java NewsFeedAppMain
``` 

Once the application starts SpreadSheetUpdaterService.fetchAndUpdate() runs every 30 minutes to update the Articles.
```$xslt
2018-12-31 16:56:10.197  INFO 1606 --- [           main] c.n.c.a.s.SpreadSheetUpdaterService 
Please verify results at https://docs.google.com/spreadsheets/d/1q1bQCdomwyCxfRLWubPRfWbmRFwfPXRgnABLw4-iwDU/edit
```

## Stack
- Java 1.8
- Spring Boot
- Lombok (Install Lombol Plugin in IntelliJ/Eclipse)
- H2


