package com.example.bff.resolver;

import com.example.bff.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GraphQLResponse {
    private Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private User user;

        public User getUser() { return user; }
        public void setUser(User user) { this.user = user; }
    }

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }
}
