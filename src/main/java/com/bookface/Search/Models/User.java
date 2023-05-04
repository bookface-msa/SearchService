package com.bookface.Search.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "users")
public class User {
    @Id
    private String id;
    @Field(type = FieldType.Text, name = "username")
    private String username;

    @Field(type = FieldType.Text, name = "bio")
    private String bio;

    @Field(type = FieldType.Text, name = "imageurl")
    private String imageurl;

    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public String getImageurl() {
        return imageurl;
    }
}
