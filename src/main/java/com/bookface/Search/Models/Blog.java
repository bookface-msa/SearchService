package com.bookface.Search.Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.context.properties.bind.Nested;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.MultiField;

import java.util.ArrayList;
import java.util.List;

@Document(indexName = "blogs")
public class Blog {

    @Id
    @Field(type = FieldType.Text, name = "id")
    private String id;
    @Field(type = FieldType.Text, name = "title")
    private String title;

    @Field(type = FieldType.Date, name = "date")
    private String date;

    @Field(type = FieldType.Text, name = "author-id")
    @JsonProperty("author-id")
    private String authorId;

    @Field(type = FieldType.Text, name = "content")
    private String content;

    @Field(type = FieldType.Nested, name = "tags")
    private ArrayList<Tag> tags;


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}
