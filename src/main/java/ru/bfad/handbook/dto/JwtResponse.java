package ru.bfad.handbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String admin;

    public JwtResponse(String token) {
        this.token = token;
    }
}
