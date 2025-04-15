package com.ecommerce.user_service.controller;

import com.ecommerce.user_service.dto.UserRequest;
import com.ecommerce.user_service.dto.UserResponse;
import com.ecommerce.user_service.service.IUserService;
import com.ecommerce.user_service.util.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping
    public ResponseEntity<ApiResponseUtil<List<UserResponse>>> findAll() {
        List<UserResponse> response = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponseUtil<>(true, "Ususarios listados con exito!", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseUtil<UserResponse>> findById(@PathVariable("id") Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(new ApiResponseUtil<>(true, "Usuario encontrado con exito!", response));
    }

    @PostMapping()
    public ResponseEntity<ApiResponseUtil<UserResponse>> register(@RequestBody @Valid UserRequest userRequest) {
        try {
            UserResponse response = userService.registerUser(userRequest);
            ApiResponseUtil<UserResponse> apiResponse = new ApiResponseUtil<>(true, "Usuario creado con exito!", response);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponseUtil<UserResponse> apiResponse = new ApiResponseUtil<>(false, "Error al registrar el usuario", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseUtil<UserResponse>> deleteById(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new ApiResponseUtil<>(true, "Usuario eliminado con exito!", null));
    }

}
