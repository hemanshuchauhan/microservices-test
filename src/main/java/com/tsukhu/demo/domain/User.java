package com.tsukhu.demo.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private String id;

    private String phone;

    private String username;

    private String website;

    private Address address;

    private String email;

    private Company company;

    private String name;
}
