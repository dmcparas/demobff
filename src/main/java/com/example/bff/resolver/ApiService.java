package com.example.bff.resolver;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

@Service
public class ApiService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Callable<String> callApi(String url) {
        return () -> restTemplate.getForObject(url, String.class);
    }
}
