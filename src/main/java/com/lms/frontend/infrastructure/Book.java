package com.lms.frontend.infrastructure;


import lombok.Data;
import lombok.ToString;

import java.util.Date;


@Data
@ToString
public class Book {


    private Long id;

    private String title;
    private String author;
    private String isbn;
    private Date publishedDate;
}
