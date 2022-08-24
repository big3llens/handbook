package ru.bfad.handbook.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class MessageFrom {
    private String from;
    private String subject;
    private String text;
    private Integer mailslistId;
    private Integer deliveryMethod;
}
