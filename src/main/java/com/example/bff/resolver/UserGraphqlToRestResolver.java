package com.example.bff.resolver;

import com.example.bff.model.Constant;
import com.example.bff.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Controller
public class UserGraphqlToRestResolver {

    private final RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();
    private final ApiService apiService = new ApiService();
    String baseUrl1 = Constant.URL1;
    String baseUrl2 = Constant.URL2;
    String baseUrl3 = Constant.URL3;

    @QueryMapping
    public User getGraphQLToRestSingle() {
        String url = baseUrl1 + "/api/users/1";
        return restTemplate.getForObject(url, User.class);
    }

    @QueryMapping
    public User getGraphQLToRestSequence() {
        String url1 = baseUrl1 + "/api/users/1";
        String url2 = baseUrl2 + "/api/users/2";
        String url3 = baseUrl3 + "/api/users/3";
        User user;
        user = restTemplate.getForObject(url1, User.class);
        user = restTemplate.getForObject(url2, User.class);
        user = restTemplate.getForObject(url3, User.class);
        return user;
    }

    @QueryMapping
    public List<String> getGraphQLToRestParallel() throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            // Define API URLs
            String url1 = baseUrl1 + "/api/users/1";
            String url2 = baseUrl2 + "/api/users/2";
            String url3 = baseUrl3 + "/api/users/3";

            // Create callables for each API
            List<Callable<String>> tasks = Arrays.asList(
                    apiService.callApi(url1),
                    apiService.callApi(url2),
                    apiService.callApi(url3)
            );

            // Invoke all tasks in parallel
            List<Future<String>> futures = executor.invokeAll(tasks);

            // Collect all responses
            return futures.stream()
                    .map(f -> {
                        try {
                            return f.get();
                        } catch (Exception e) {
                            return "Error: " + e.getMessage();
                        }
                    })
                    .collect(Collectors.toList());

        } finally {
            executor.shutdown();
        }
    }
}
