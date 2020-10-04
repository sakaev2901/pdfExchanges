package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private String name;
    private String surname;
    private String patronymic;
    private String birthday;
    private String passwordId;

    public static User from(String s) {
        String[] parts = s.split(" ");
        return User.builder()
                .name(parts[0])
                .surname(parts[1])
                .patronymic(parts[2])
                .birthday(parts[3])
                .passwordId(parts[4])
                .build();
    }
}
