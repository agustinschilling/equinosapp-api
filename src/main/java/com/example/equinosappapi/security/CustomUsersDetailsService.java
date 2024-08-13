package com.example.equinosappapi.security;

import com.example.equinosappapi.models.Role;
import com.example.equinosappapi.models.User;
import com.example.equinosappapi.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUsersDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;

    @Autowired
    public CustomUsersDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Collection<GrantedAuthority> mapToAuthorities(Role role) {
        return role.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.toString())).collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapToAuthorities(user.getRole()));
    }
}
