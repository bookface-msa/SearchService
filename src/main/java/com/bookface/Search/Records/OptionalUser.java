package com.bookface.Search.Records;

import com.bookface.Search.Models.User;
import lombok.Builder;

@Builder
public record OptionalUser(boolean isNull, User user) {
}
