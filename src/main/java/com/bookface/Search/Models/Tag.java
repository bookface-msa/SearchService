package com.bookface.Search.Models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "tags")
public class Tag {
    @Id
    @Field(type = FieldType.Text)
    private String tag;

    @Field(type = FieldType.Integer)
    private int relevancy;
}
