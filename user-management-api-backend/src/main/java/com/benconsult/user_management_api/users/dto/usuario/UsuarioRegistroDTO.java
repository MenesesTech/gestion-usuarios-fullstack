package com.benconsult.user_management_api.users.dto.usuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRegistroDTO(

        @NotBlank(message = "El nombre de usuario es obligatorio")
        String username,

        @NotBlank(message = "El correo electrónico es obligatorio")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password,

        @NotNull(message = "El estado es obligatorio")
        Boolean estado
) {}

