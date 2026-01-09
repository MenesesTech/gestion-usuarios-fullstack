package com.benconsult.user_management_api.users.mapper;

import com.benconsult.user_management_api.users.dto.usuario.UsuarioRegistroDTO;
import com.benconsult.user_management_api.users.dto.usuario.UsuarioResponseDTO;
import com.benconsult.user_management_api.users.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    // DTO ➜ ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", ignore = true)
    Usuario toEntity(UsuarioRegistroDTO dto);

    // ENTITY ➜ DTO
    UsuarioResponseDTO toDto(Usuario entity);

    List<UsuarioResponseDTO> toDtoList(List<Usuario> entities);
}
