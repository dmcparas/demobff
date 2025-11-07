package com.example.bff.resolver;

import com.example.bff.model.User;
import com.example.core.grpc.UserResponse;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
@CrossOrigin(origins = "*")
public class UserGraphqlToGRPCResolver {
    private final UserClientService userClientService;

    public UserGraphqlToGRPCResolver(UserClientService userClientService) {
        this.userClientService = userClientService;
    }

    @QueryMapping
    public User getGraphQLToGRPCSingle() {
        UserResponse response = userClientService.getUser(String.valueOf(1));
        User dto = new User(response.getId(), response.getName(), response.getEmail(), 55);
        return dto;
    }

    @QueryMapping
    public User getGraphQLToGRPCSequence() {
        UserResponse response;
        response = userClientService.getUser(String.valueOf(1));
        response = userClientService.getUser(String.valueOf(2));
        response = userClientService.getUser(String.valueOf(3));
        User dto = new User(response.getId(), response.getName(), response.getEmail(), 55);
        return dto;
    }

    @QueryMapping
    public User getGraphQLToGRPCParallel() {
        // Launch gRPC calls in parallel
        CompletableFuture<UserResponse> future1 = CompletableFuture.supplyAsync(() -> userClientService.getUser(String.valueOf(1)));
        CompletableFuture<UserResponse> future2 = CompletableFuture.supplyAsync(() -> userClientService.getUser(String.valueOf(2)));
        CompletableFuture<UserResponse> future3 = CompletableFuture.supplyAsync(() -> userClientService.getUser(String.valueOf(3)));

        // Wait for all to complete
        CompletableFuture<Void> allDone = CompletableFuture.allOf(future1, future2, future3);

        try {
            allDone.get(); // Wait for all futures to complete

            // Example: return the last response as DTO
            UserResponse response = future3.get();
            User dto = new User(response.getId(), response.getName(), response.getEmail(), 55);
            return dto;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
