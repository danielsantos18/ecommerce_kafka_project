package com.ecommerce.product_service.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProducNotFoundException extends RuntimeException {
    public ProducNotFoundException(String message) {
        super(message);
    }
}
