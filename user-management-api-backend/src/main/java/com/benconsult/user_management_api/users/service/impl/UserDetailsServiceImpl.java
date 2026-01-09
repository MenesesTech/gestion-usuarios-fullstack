package com.benconsult.user_management_api.users.service.impl;

import com.benconsult.user_management_api.users.entity.Usuario;
import com.benconsult.user_management_api.users.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository userRepository;

    public UserDetailsServiceImpl(UsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado: " + username
                        )
                );
        return User.withUsername(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(new ArrayList<>())
                .build();
    }
}
