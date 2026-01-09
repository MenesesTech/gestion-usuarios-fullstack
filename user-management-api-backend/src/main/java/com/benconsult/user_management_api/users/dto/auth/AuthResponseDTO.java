package com.benconsult.user_management_api.users.dto.auth;

public record AuthResponseDTO(
        String token,
        String refreshToken
) {}
