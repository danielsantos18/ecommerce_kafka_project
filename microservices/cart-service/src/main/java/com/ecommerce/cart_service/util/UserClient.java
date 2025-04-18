package com.ecommerce.cart_service.util;

import com.ecommerce.cart_service.dto.UserApiResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public UserApiResponse getUserById(Long userId) {
        String url = "http://localhost:8080/api/users/" + userId;

        try {
            // Obtener la respuesta completa mapeada a UserApiResponse
            UserApiResponse response = restTemplate.getForObject(url, UserApiResponse.class);
            System.out.println("Respuesta completa del UserService: " + response);

            // Devolver el objeto completo
            return response;
        } catch (HttpClientErrorException e) {
            System.err.println("Error al obtener datos del usuario: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error general al obtener datos del usuario: " + e.getMessage());
            return null;
        }
    }
}
