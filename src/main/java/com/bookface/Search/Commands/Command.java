package com.bookface.Search.Commands;

import java.util.HashMap;

public interface Command<A,B> {
    public A execute(B t);
}
