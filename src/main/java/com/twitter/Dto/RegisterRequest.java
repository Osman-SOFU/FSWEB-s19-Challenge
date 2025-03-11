package com.twitter.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank
    @NotNull
    @Email
    private String email;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String surname;
}
