package ru.bfad.handbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonToMailslist {
    private Integer id;
    private String email;
    private String fullName;
}
