package com.example.bff.resolver;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import com.example.core.grpc.UserServiceGrpc;
import com.example.core.grpc.UserRequest;
import com.example.core.grpc.UserResponse;


@Service
public class UserClientService {
    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceStub;

    public UserResponse getUser(String id) {
        UserRequest request = UserRequest.newBuilder().setId(id).build();
        return userServiceStub.getUser(request);
    }
}
