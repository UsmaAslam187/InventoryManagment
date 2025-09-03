package com.techfoot.stockspree.Utils;

public class golbalMethods {
    private final Integer start;
    private final Integer end;

    public golbalMethods(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    public Integer getStart() {
        return start;
    }

    public Integer getEnd() {
        return end;
    }

    public static golbalMethods getPage(int pageNumber, int size, int count) {
        int start = pageNumber * size;
        int end = 10;
        return new golbalMethods(start, end);
    }
}
