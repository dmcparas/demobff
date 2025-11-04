package com.example.bff.resolver;

import com.example.bff.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Controller
public class UserQueryResolver {

    private final RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();
    private final ApiService apiService = new ApiService();
    String baseUrl1 = "https://democore.onrender.com";
    String baseUrl2 = "https://democore-1.onrender.com";
    String baseUrl3 = "https://democore-2.onrender.com";

    @QueryMapping
    public User getUserInfo(@Argument String userId) {
        String url = baseUrl1 + "/api/users/" + userId;
        return restTemplate.getForObject(url, User.class);
    }

    @QueryMapping
    public User getGraphQLSequence() {
            String url1 = baseUrl1 + "/graphql";

            String query1 = "query { user(id: \"123\") { id name email } }";

            String url2 = baseUrl2 + "/graphql";

            String query2 = "query { user(id: \"456\") { id name email } }";

            String url3 = baseUrl3 + "/graphql";

            String query3 = "query { user(id: \"789\") { id name email } }";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("query", query1);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url1, request, String.class);

            requestBody.put("query", query2);
            request = new HttpEntity<>(requestBody, headers);
            response = restTemplate.postForEntity(url2, request, String.class);

            requestBody.put("query", query3);
            request = new HttpEntity<>(requestBody, headers);
            response = restTemplate.postForEntity(url3, request, String.class);

            // Deserialize JSON to GraphQLResponse
            GraphQLResponse graphQLResponse = null;
            try {
                graphQLResponse = objectMapper.readValue(response.getBody(), GraphQLResponse.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return graphQLResponse.getData() != null ? graphQLResponse.getData().getUser() : null;
    }

    @QueryMapping
    public User getGraphQLParallel() {
        String url1 = baseUrl1 + "/graphql";
        String url2 = baseUrl2 + "/graphql";
        String url3 = baseUrl3 + "/graphql";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create a CompletableFuture for each GraphQL request
        CompletableFuture<ResponseEntity<String>> future1 = CompletableFuture.supplyAsync(() -> {
            Map<String, Object> requestBody = Map.of("query", "query { user(id: \"123\") { id name email } }");
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            return restTemplate.postForEntity(url1, request, String.class);
        });

        CompletableFuture<ResponseEntity<String>> future2 = CompletableFuture.supplyAsync(() -> {
            Map<String, Object> requestBody = Map.of("query", "query { user(id: \"456\") { id name email } }");
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            return restTemplate.postForEntity(url2, request, String.class);
        });

        CompletableFuture<ResponseEntity<String>> future3 = CompletableFuture.supplyAsync(() -> {
            Map<String, Object> requestBody = Map.of("query", "query { user(id: \"789\") { id name email } }");
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            return restTemplate.postForEntity(url3, request, String.class);
        });

        // Wait for all three futures to complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2, future3);

        // Wait and handle the results
        allFutures.join();

        try {
            ResponseEntity<String> response = future3.get(); // Or future2.get(), future3.get()
            GraphQLResponse graphQLResponse = objectMapper.readValue(response.getBody(), GraphQLResponse.class);
            return graphQLResponse.getData() != null ? graphQLResponse.getData().getUser() : null;
        } catch (InterruptedException | ExecutionException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @QueryMapping
    public User getSequence() {
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
    public List<String> getParallel() throws InterruptedException, ExecutionException {

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
