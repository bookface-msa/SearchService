package com.bookface.Search.Models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Getter
@Setter
@Document(indexName = "tags")
public class Tag {
    @Id
    @Field(type = FieldType.Text)
    private String tag;

    @Field(type = FieldType.Integer)
    private int relevancy;
}
