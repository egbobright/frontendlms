package com.lms.frontend.infrastructure;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class BookService {
    private static final int TIMEOUT = 30;
    private static final String baseUrl = "http://localhost:9190";
    private RestTemplate restTemplate = null;

    public BookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ApiBaseResponse fetchBook(Long id){
        String url = baseUrl + "/books";
        try{
            return restTemplate.getForObject(url, ApiBaseResponse.class);
        }catch (Exception ex){
            return null;
        }
    }

    public List<Book> fetchBooks(){
        String url = baseUrl + "/book";
        try{
            ResponseEntity<List<Book>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    null
                    //new ParameterizedTypeReference<List<Book>>() {}
            );
            var response = responseEntity.getBody();
            return response;
        }catch (Exception ex){
            return null;
        }
    }
}
