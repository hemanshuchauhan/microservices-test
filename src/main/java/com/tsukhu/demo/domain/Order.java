package com.tsukhu.demo.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {
    private String name;
    private String sku;
    private Address shipTo;
    private Address billTo;
}
