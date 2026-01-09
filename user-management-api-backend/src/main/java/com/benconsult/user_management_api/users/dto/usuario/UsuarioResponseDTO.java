package com.benconsult.user_management_api.users.dto.usuario;

import java.time.LocalDateTime;

public record UsuarioResponseDTO(
        Long id,
        String username,
        String email,
        String password,
        Boolean estado,
        LocalDateTime fechaCreacion
) {
}
