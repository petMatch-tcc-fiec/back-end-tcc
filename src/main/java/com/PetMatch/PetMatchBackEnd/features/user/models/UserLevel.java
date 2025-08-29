package com.PetMatch.PetMatchBackEnd.features.user.models;

import org.springframework.security.core.GrantedAuthority;

public enum UserLevel implements GrantedAuthority {
    USER,
    ADMIN,
    ONG;

    @Override
    public String getAuthority() {
        return this.name();
    }
}