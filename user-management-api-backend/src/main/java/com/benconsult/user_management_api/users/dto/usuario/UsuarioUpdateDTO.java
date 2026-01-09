package com.benconsult.user_management_api.users.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record UsuarioUpdateDTO(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        String username,
        @NotBlank(message = "El correo electrónico es obligatorio")
        String email,
        String password,
        Boolean estado
) {
}
