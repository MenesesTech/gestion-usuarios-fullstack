package com.benconsult.user_management_api.users.controller;

import com.benconsult.user_management_api.users.dto.usuario.UsuarioRegistroDTO;
import com.benconsult.user_management_api.users.dto.usuario.UsuarioResponseDTO;
import com.benconsult.user_management_api.users.dto.usuario.UsuarioUpdateDTO;
import com.benconsult.user_management_api.users.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UsuarioService usuarioService;

    public UserController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> anadirUsuario(@Validated @RequestBody UsuarioRegistroDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrarUsuario(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> modificarUsuario(@PathVariable Long id,@Validated @RequestBody UsuarioUpdateDTO dto){
        return ResponseEntity.ok(usuarioService.modificarUsuario(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarUsuario(@PathVariable Long id){
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioId(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }
}
