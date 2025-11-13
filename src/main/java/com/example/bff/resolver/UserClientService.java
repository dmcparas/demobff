package com.example.bff.resolver;

import com.example.core.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;


@Service
public class UserClientService {
    @GrpcClient("democore")
    private UserServiceGrpc.UserServiceBlockingStub userServiceStub;

    // Async stub for streaming RPC
    @GrpcClient("democore")
    private UserServiceGrpc.UserServiceStub userServiceAsyncStub;

    public UserResponse getUser(String id) {
        UserRequest request = UserRequest.newBuilder().setId(id).build();
        return userServiceStub.getUser(request);
    }

    public void uploadFile(String filePath) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("File not found: " + filePath);
        }

        // Response observer to handle server response
        StreamObserver<FileUploadResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(FileUploadResponse value) {
                System.out.println("Server response: " + value.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("Upload completed successfully.");
            }
        };

        // Request observer to send file chunks
        StreamObserver<FileUploadRequest> requestObserver = userServiceAsyncStub.uploadFile(responseObserver);

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int read;
            boolean isFirst = true;

            while ((read = fis.read(buffer)) != -1) {
                FileUploadRequest.Builder builder = FileUploadRequest.newBuilder()
                        .setContent(com.google.protobuf.ByteString.copyFrom(buffer, 0, read));

                if (isFirst) {
                    builder.setFilename(file.getName());
                    isFirst = false;
                }

                requestObserver.onNext(builder.build());
            }
        } catch (Exception e) {
            requestObserver.onError(e);
            throw e;
        }

        // Signal completion
        requestObserver.onCompleted();
    }
}
