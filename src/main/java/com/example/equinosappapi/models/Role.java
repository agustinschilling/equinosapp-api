package com.example.equinosappapi.models;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    USER, ADVANCED_USER;

    public List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        roles.add(Role.ADVANCED_USER);
        return roles;
    }
}
