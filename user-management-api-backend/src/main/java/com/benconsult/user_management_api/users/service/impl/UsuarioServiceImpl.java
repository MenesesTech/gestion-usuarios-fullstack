package com.benconsult.user_management_api.users.service.impl;

import com.benconsult.user_management_api.users.dto.usuario.UsuarioRegistroDTO;
import com.benconsult.user_management_api.users.dto.usuario.UsuarioResponseDTO;
import com.benconsult.user_management_api.users.dto.usuario.UsuarioUpdateDTO;
import com.benconsult.user_management_api.users.entity.Usuario;
import com.benconsult.user_management_api.users.exception.*;
import com.benconsult.user_management_api.users.mapper.UsuarioMapper;
import com.benconsult.user_management_api.users.repository.UsuarioRepository;
import com.benconsult.user_management_api.users.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private UsuarioMapper usuarioMapper;


    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public UsuarioResponseDTO registrarUsuario(UsuarioRegistroDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setPassword(passwordEncoder.encode(dto.password()));

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuarioGuardado);
    }

    @Override
    public UsuarioResponseDTO modificarUsuario(Long id, UsuarioUpdateDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: "+id));
        usuarioExistente.setUsername(dto.username());
        usuarioExistente.setEmail(dto.email());

        if (dto.password() != null && !dto.password().isBlank()) {
            usuarioExistente.setPassword(passwordEncoder.encode(dto.password()));
        }

        if (dto.estado() != null) {
            usuarioExistente.setEstado(dto.estado());
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuarioExistente);
        return usuarioMapper.toDto(usuarioActualizado);
    }

    @Override
    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + id));
        return usuarioMapper.toDto(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarioMapper.toDtoList(usuarios);
    }

    @Override
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
