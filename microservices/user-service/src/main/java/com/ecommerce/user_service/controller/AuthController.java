package com.ecommerce.user_service.controller;

import com.ecommerce.user_service.dto.AuthRequest;
import com.ecommerce.user_service.dto.UserRequest;
import com.ecommerce.user_service.dto.UserResponse;
import com.ecommerce.user_service.model.User;
import com.ecommerce.user_service.repo.IUserRepo;
import com.ecommerce.user_service.service.IUserService;
import com.ecommerce.user_service.util.ApiResponseUtil;
import com.ecommerce.user_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final IUserService userService;
    private final AuthenticationManager authManager;
    private final IUserRepo userRepository;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseUtil<UserResponse>> register(@RequestBody @Validated UserRequest userRequest) {
        UserResponse response = userService.registerUser(userRequest);
        ApiResponseUtil<UserResponse> apiResponse = new ApiResponseUtil<>(true, "Usuario creado con exito!", response);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(Map.of("token", token));
    }
}
