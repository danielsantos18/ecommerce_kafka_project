package com.ecommerce.user_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisteredEvent {
    private Long id;
    private String name;
    private String lastname;
    private String phone;
    private String email;
    //private String password;
}
