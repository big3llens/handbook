package ru.bfad.handbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bfad.handbook.models.Person;

import java.util.Optional;

public interface PersonRepositories extends JpaRepository<Person, Integer> {

    Optional<Person> findPersonByEmail(String email);

    Boolean existsByEmail(String email);
}
