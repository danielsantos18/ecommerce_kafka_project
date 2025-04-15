package com.ecommerce.user_service.repo;

import com.ecommerce.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepo extends JpaRepository<User, Long> {

}
