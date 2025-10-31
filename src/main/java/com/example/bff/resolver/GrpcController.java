package com.example.bff.resolver;

import com.example.bff.model.User;
import com.example.core.grpc.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/users/grpc")
public class GrpcController {

    private final UserClientService userClientService;

    public GrpcController(UserClientService userClientService) {
        this.userClientService = userClientService;
    }

    @GetMapping("/sequence/{id}")
    public ResponseEntity<User> getUserSequence(@PathVariable String id) {
        UserResponse response = userClientService.getUser(id);
        response = userClientService.getUser(id+1);
        response = userClientService.getUser(id+2);
        User dto = new User(response.getId(), response.getName(), response.getEmail());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/parallel/{id}")
    public ResponseEntity<User> getUserParallel(@PathVariable String id) {
        // Launch gRPC calls in parallel
        CompletableFuture<UserResponse> future1 = CompletableFuture.supplyAsync(() -> userClientService.getUser(id));
        CompletableFuture<UserResponse> future2 = CompletableFuture.supplyAsync(() -> userClientService.getUser(id + "1"));
        CompletableFuture<UserResponse> future3 = CompletableFuture.supplyAsync(() -> userClientService.getUser(id + "2"));

        // Wait for all to complete
        CompletableFuture<Void> allDone = CompletableFuture.allOf(future1, future2, future3);

        try {
            allDone.get(); // Wait for all futures to complete

            // Example: return the last response as DTO
            UserResponse response = future3.get();
            User dto = new User(response.getId(), response.getName(), response.getEmail());
            return ResponseEntity.ok(dto);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}
