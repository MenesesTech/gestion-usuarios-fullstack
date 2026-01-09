package com.benconsult.user_management_api.users.service;

import com.benconsult.user_management_api.users.dto.usuario.UsuarioRegistroDTO;
import com.benconsult.user_management_api.users.dto.usuario.UsuarioResponseDTO;
import com.benconsult.user_management_api.users.dto.usuario.UsuarioUpdateDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDTO registrarUsuario(UsuarioRegistroDTO dto);

    UsuarioResponseDTO modificarUsuario(Long id, UsuarioUpdateDTO dto);

    UsuarioResponseDTO obtenerUsuarioPorId(Long id);

    List<UsuarioResponseDTO> listarUsuarios();

    void eliminarUsuario(Long id);
}
