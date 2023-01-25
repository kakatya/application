package ru.kakatya.application.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

class ErrorMessageTest {

    @Test
    void getStatusCode() {
        ErrorMessage errorMessage = new ErrorMessage(12, new Date(123445L), "hello", "hello");
        Assertions.assertEquals(12,errorMessage.getStatusCode());
    }

    @Test
    void getTimestamp() {
        ErrorMessage errorMessage = new ErrorMessage(12, new Date(123445L), "hello", "hello");
        Assertions.assertEquals(new Date(123445L),errorMessage.getTimestamp());
    }

    @Test
    void getMessage() {
        ErrorMessage errorMessage = new ErrorMessage(12, new Date(123445L), "hello", "hello");
        Assertions.assertEquals("hello",errorMessage.getMessage());
    }

    @Test
    void getDescription() {
        ErrorMessage errorMessage = new ErrorMessage(12, new Date(123445L), "hello", "hello");
        Assertions.assertEquals("hello",errorMessage.getDescription());
    }
}