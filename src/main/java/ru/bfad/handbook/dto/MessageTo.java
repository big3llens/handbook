package ru.bfad.handbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageTo {
    private String from;
    private String subject;
    private String text;
    private Collection<String> to;
    private Integer deliveryMethod;
}
