package ru.bfad.handbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bfad.handbook.models.PersonMailslist;

public interface PersonMaillistRepositories extends JpaRepository<PersonMailslist, Long> {
    

}
