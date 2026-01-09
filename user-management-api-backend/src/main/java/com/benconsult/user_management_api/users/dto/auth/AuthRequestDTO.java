package com.benconsult.user_management_api.users.dto.auth;

public record AuthRequestDTO(
        String username,
        String password
) {}
