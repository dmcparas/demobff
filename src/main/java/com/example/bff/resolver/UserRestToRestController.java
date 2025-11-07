package com.example.bff.resolver;


import com.example.bff.model.Constant;
import com.example.bff.model.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserRestToRestController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ApiService apiService = new ApiService();
    String baseUrl1 = Constant.URL1;
    String baseUrl2 = Constant.URL2;
    String baseUrl3 = Constant.URL3;

    @GetMapping("/single")
    public User getUserInfo(@RequestParam(required = false, defaultValue = "55") int size) {
        String url1 = baseUrl1 + "/api/users/1?size=" + size;
        User user;
        user = restTemplate.getForObject(url1, User.class);
        return user;
    }

    @GetMapping("/sequence")
    public User getUserInfoSequence(@RequestParam(required = false, defaultValue = "55") int size) {
        String url1 = baseUrl1 + "/api/users/1?size=" + size;
        String url2 = baseUrl2 + "/api/users/2?size=" + size;
        String url3 = baseUrl3 + "/api/users/3?size=" + size;
        User user;
        user = restTemplate.getForObject(url1, User.class);
        user = restTemplate.getForObject(url2, User.class);
        user = restTemplate.getForObject(url3, User.class);
        return user;
    }

    @GetMapping("/parallel")
    public List<String> getUserInfoParallel(@RequestParam(required = false, defaultValue = "55") int size) throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            // Define API URLs
            String url1 = baseUrl1 + "/api/users/1?size=" + size;
            String url2 = baseUrl2 + "/api/users/2?size=" + size;
            String url3 = baseUrl3 + "/api/users/3?size=" + size;

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
