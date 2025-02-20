package com.main.exception;

public class InvalidBudgetException extends RuntimeException {
    public InvalidBudgetException(String message) {
        super(message);
    }
}

