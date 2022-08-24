package ru.bfad.handbook.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.bfad.handbook.models.From;

import java.util.List;

@Data
@AllArgsConstructor
public class ListsInit {

    private List<MailslistDto> mailslist;

    private List<From> listFrom;
}
