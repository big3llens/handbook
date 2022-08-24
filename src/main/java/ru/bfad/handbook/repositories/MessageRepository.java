package ru.bfad.handbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bfad.handbook.models.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
