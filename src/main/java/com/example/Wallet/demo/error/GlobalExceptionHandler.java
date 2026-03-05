package com.example.Wallet.demo.error;

import com.example.Wallet.demo.exception.DuplicateResourceException;
import com.example.Wallet.demo.exception.InsufficientFundsException;
import com.example.Wallet.demo.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // ata ki kore
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String ,String>> handleNotFound(ResourceNotFoundException ex){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));

    }
    @ExceptionHandler(IllegalArgumentException.class)  //ata o ki kore
    public ResponseEntity<Map<String ,String>> handleBadRequest(IllegalArgumentException ex){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));

    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String ,String>> handleValidation(MethodArgumentNotValidException ex){

        Map<String, String> errors=new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error->errors.put(error.getField(),error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map<String ,String>> handleInsufficient(InsufficientFundsException ex){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));

    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String ,String>> handleDuplicate(DuplicateResourceException ex){

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));

    }
}
