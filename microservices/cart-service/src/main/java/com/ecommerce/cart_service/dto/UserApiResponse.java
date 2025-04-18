package com.ecommerce.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserApiResponse {
    private boolean success;
    private String message;
    private UserResponse data;

    @Data
    public static class UserResponse {
        private Long id;
        private String name;
        private String lastname;
        private String email;
        private String phone;
    }
}