package com.example.bff.resolver;

import com.example.bff.model.User;
import com.example.core.grpc.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/users/grpc")
@CrossOrigin(origins = "*")
public class UserRestToGrpcController {

    private final UserClientService userClientService;

    public UserRestToGrpcController(UserClientService userClientService) {
        this.userClientService = userClientService;
    }

    @GetMapping("/single")
    public ResponseEntity<User> getUserSingle() {
        UserResponse response = userClientService.getUser(String.valueOf(1));
        User dto = new User(response.getId(), response.getName(), response.getEmail());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/sequence")
    public ResponseEntity<User> getUserSequence() {
        UserResponse response;
        response = userClientService.getUser(String.valueOf(1));
        response = userClientService.getUser(String.valueOf(2));
        response = userClientService.getUser(String.valueOf(3));
        User dto = new User(response.getId(), response.getName(), response.getEmail());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/parallel")
    public ResponseEntity<User> getUserParallel() {
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
            User dto = new User(response.getId(), response.getName(), response.getEmail());
            return ResponseEntity.ok(dto);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

}
