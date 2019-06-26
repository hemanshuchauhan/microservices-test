package com.tsukhu.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {
    protected Integer code;
    protected String type;
    protected String message;
}
