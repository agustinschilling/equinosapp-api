package com.example.equinosappapi.services;

import com.example.equinosappapi.models.Usuario;
import com.example.equinosappapi.repositories.IUsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private IUsuariosRepository usuariosRepository;

    @Autowired
    public UsuarioService(IUsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    public void add(Usuario usuario) {
        usuariosRepository.save(usuario);
    }

    public Optional<Usuario> readOne(Long id) {
        return usuariosRepository.findById(id);
    }

    public void update(Usuario usuario) {
        usuariosRepository.save(usuario);
    }

    public void delete(Long id) {
        usuariosRepository.deleteById(id);
    }

    public Usuario getById(Long id) {
        return usuariosRepository.getReferenceById(id);
    }

    public Usuario getByUsername(String username) {
        return usuariosRepository.getByUsername(username);
    }
}
