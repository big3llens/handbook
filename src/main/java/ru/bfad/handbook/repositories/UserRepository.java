package ru.bfad.handbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bfad.handbook.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findUserByUsername(String name);
}
