package com.bookface.Search.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Document(indexName = "blogs")
public class Blog {

    @Id
    @Field(type = FieldType.Text, name = "id")
    private String id;
    @Field(type = FieldType.Text, name = "title")
    private String title;

    @Field(type = FieldType.Date, name = "date", format = DateFormat.date_hour_minute_second)
    private LocalDateTime date;

    @Field(type = FieldType.Text, name = "author-id")
    @JsonProperty("author-id")
    private String authorId;

    @Field(type = FieldType.Text, name = "content")
    private String content;

    @Field(type = FieldType.Text, name = "tags")
    private String[] tags;


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }


}
