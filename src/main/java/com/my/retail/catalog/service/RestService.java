package com.my.retail.catalog.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {
    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getProductFromRedsky(String productId) {
        try{
            String url = "https://redsky.target.com/v3/pdp/tcin/" + productId + "?excludes=taxonomy,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics&key=candidate";
            return this.restTemplate.getForObject(url, String.class);
        }
        catch(Exception hcee)
        {
            //Product not found at RedSky
            return null;
        }
    }
}