package com.ecommerce.user_service.service.impl;

import com.ecommerce.user_service.dto.UserRequest;
import com.ecommerce.user_service.dto.UserResponse;
import com.ecommerce.user_service.event.UserDeletedEvent;
import com.ecommerce.user_service.event.UserRegisteredEvent;
import com.ecommerce.user_service.model.User;
import com.ecommerce.user_service.model.exception.UserNotFoundException;
import com.ecommerce.user_service.repo.IUserRepo;
import com.ecommerce.user_service.service.IUserService;
import com.ecommerce.user_service.util.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepo userRepository;
    private final Producer kafkaProducer;

    @Override
    public UserResponse registerUser(UserRequest request) {
        User user = User.builder()
                .name(request.getName())
                .lastname(request.getLastname())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        User saved = userRepository.save(user);

        // Emitir evento a Kafka
        kafkaProducer.sendUserRegisteredEvent(
                new UserRegisteredEvent(saved.getId(), saved.getName(), saved.getLastname(), saved.getPhone(), saved.getEmail(), saved.getPassword())
        );
        return mapToUserResponse(saved);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    /*

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        existingUser.setName(request.getName());
        existingUser.setLastname(request.getLastname());
        existingUser.setPhone(request.getPhone());
        existingUser.setEmail(request.getEmail());
        existingUser.setPassword(request.getPassword());

        User updatedUser = userRepository.save(existingUser);

        kafkaProducer.sendUserUpdatedEvent(
                new UserUpdatedEvent(
                        updatedUser.getId(),
                        updatedUser.getName(),
                        updatedUser.getLastname(),
                        updatedUser.getPhone(),
                        updatedUser.getEmail(),
                        updatedUser.getPassword()
                )
        );

        return mapToUserResponse(updatedUser);
    }
     */

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
        kafkaProducer.sendUserDeletedEvent(
                new UserDeletedEvent(user.getId())
        );
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .phone(user.getPhone())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
