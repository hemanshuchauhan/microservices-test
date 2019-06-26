package com.tsukhu.demo.com.service;

import com.tsukhu.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Value("${app.user.path}")
    private String userBasePath;

    @Value("${app.user.uri}")
    private String userBaseURI;

    private RestTemplate restTemplate;

    @Autowired
    public OrderService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public User fetchUserById(Integer userId) {
       return restTemplate.getForObject(userBaseURI + userBasePath + "/"+ userId, User.class);
    }
}
