package ru.bfad.handbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bfad.handbook.models.From;

public interface FromRepository extends JpaRepository<From, Integer> {

    From findFromByEmail(String email);
}
