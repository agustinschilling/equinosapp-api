package com.example.equinosappapi.models;

public enum Role {
    USER, ADVANCED_USER;

    public String toString() {
        // Return the role name prefixed with "ROLE_"
        return "ROLE_" + this.name();
    }
}
