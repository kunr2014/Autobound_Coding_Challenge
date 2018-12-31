package com.neetika.challenge.autobound.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class NewsAPIResponse {

    private String status;
    private Long totalResults;
    private List<Article> articles;

    @Data
    @ToString
    @NoArgsConstructor
    public static class Source {
        private String id;
        private String name;
    }

    @Data
    @ToString
    @NoArgsConstructor
    public static class Article {
        private Source source;
        private String author;
        private String title;
        private String description;
        private String url;
        private String urlToImage;
        private Date publishedAt;
        private String content;
    }

}
