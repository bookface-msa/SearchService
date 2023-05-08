package com.bookface.Search.Models;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class Tag {
    @Field(type = FieldType.Text)
    String tag;

    public String getTag() {
        return tag;
    }

    public Tag(String tag) {
        this.tag = tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
