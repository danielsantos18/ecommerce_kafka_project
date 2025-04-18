package com.ecommerce.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotNull
    @Size(min = 3, max = 70, message = "el name tiene un maximo de 70 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "formato de name invalido")
    private String name;

    @NotNull
    @Size(min = 3, max = 70, message = "el lastname tiene un maximo de 70 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "formato de lastname invalido")
    private String lastname;

    @NotNull
    @Size(min = 10, max = 10, message = "el telefono debe tener 10 numeros")
    private String phone;

    @NotNull
    @Email(message = "El correo debe tener un formato valido")
    @Size(max = 80, message = "el email tiene un maximo de 80 caracteres")
    private String email;

    @NotNull
    @Size(min = 8, message = "la password debe tener al menos 8 caracteres")
    private String password;
}
