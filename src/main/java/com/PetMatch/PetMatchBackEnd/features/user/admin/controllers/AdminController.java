package com.PetMatch.PetMatchBackEnd.features.user.admin.controllers;

import com.PetMatch.PetMatchBackEnd.features.user.models.Usuario;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/admin")
public class AdminController {

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/test")
    public void testRequest(Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        System.out.println(usuario);
    }
}
