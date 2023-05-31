package com.bookface.Search.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Field(type = FieldType.Text, name = "id")
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}
