package com.PetMatch.PetMatchBackEnd.features.auth.dto;

import com.PetMatch.PetMatchBackEnd.features.user.models.UserLevel;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String picture;
    private UserLevel accessLevel;
}
