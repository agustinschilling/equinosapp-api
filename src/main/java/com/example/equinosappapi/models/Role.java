package com.example.equinosappapi.models;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    USUARIO, VETERINARIO;

    public List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.USUARIO);
        roles.add(Role.VETERINARIO);
        return roles;
    }
}
