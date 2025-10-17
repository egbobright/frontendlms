package com.lms.frontend.infrastructure;

import lombok.Data;

import java.util.Date;

@Data
public class BookDTO {
    private String title;
    private String author;
    private String isbn;
    private Date publishedDate;
}
