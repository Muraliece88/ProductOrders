package com.abn.nl.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Errors {
    private int status;
    private String message;
    private LocalDateTime timeStamp;
}
