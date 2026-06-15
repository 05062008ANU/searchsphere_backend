package com.searchsphere.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "search_logs")
public class SearchLog {

    @Id
    private String id;

    private String query;
    private int resultCount;
    private String username;
    private LocalDateTime timestamp;

    public SearchLog() {}

    public SearchLog(String query, int resultCount, String username) {
        this.query = query;
        this.resultCount = resultCount;
        this.username = username;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public int getResultCount() { return resultCount; }
    public void setResultCount(int resultCount) { this.resultCount = resultCount; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}