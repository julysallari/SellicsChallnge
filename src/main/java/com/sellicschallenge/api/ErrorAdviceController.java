package com.sellicschallenge.api;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.WriteTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice("com.sellicschallenge.api")
public class ErrorAdviceController {

    private static final String message = "Sorry, we can't process your score right now. Please, try again later";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleAnyException(RuntimeException exception) {
        if (ReadTimeoutException.INSTANCE.equals(exception.getCause())) {
            return this.buildErrorResponse(HttpStatus.REQUEST_TIMEOUT);
        } else if (WriteTimeoutException.INSTANCE.equals(exception.getCause())) {
            return this.buildErrorResponse(HttpStatus.GATEWAY_TIMEOUT);
        }
        return this.buildErrorResponse(HttpStatus.SERVICE_UNAVAILABLE);
    }

    private ResponseEntity<?> buildErrorResponse(HttpStatus status) {
        return new ResponseEntity<>(JsonErrorResponse.create(message,status.getReasonPhrase()), status);
    }

    private static class JsonErrorResponse {
        public static ObjectNode create(String message, String status) {
            ObjectNode jsonNode = JsonNodeFactory.instance.objectNode();
            jsonNode.put("message", message);
            jsonNode.put("status", status);
            return jsonNode;
        }
    }
}
