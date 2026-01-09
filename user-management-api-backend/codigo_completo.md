---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\config\CorsConfig.java
---

``java

package com.benconsult.user_management_api.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\controller\AuthController.java
---

``java

package com.benconsult.user_management_api.users.controller;

import com.benconsult.user_management_api.users.dto.auth.AuthRequestDTO;
import com.benconsult.user_management_api.users.dto.auth.AuthResponseDTO;
import com.benconsult.user_management_api.users.service.JwtUtilService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtilService jwtUtilService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtUtilService jwtUtilService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtilService = jwtUtilService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody AuthRequestDTO authRequestDto) {

        // Autenticar usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequestDto.username(),
                        authRequestDto.password()
                )
        );

        // Cargar UserDetails
        UserDetails userDetails =
                userDetailsService.loadUserByUsername(
                        authRequestDto.username()
                );

        // Generar tokens
        String token = jwtUtilService.generateToken(userDetails);
        String refreshToken =
                jwtUtilService.generateRefreshToken(userDetails);

        // Responder
        return ResponseEntity.ok(
                new AuthResponseDTO(token, refreshToken)
        );
    }
}


``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\controller\UserController.java
---

``java

package com.benconsult.user_management_api.users.controller;

import com.benconsult.user_management_api.users.dto.usuario.UsuarioRegistroDTO;
import com.benconsult.user_management_api.users.dto.usuario.UsuarioResponseDTO;
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
    public ResponseEntity<UsuarioResponseDTO> modificarUsuario(@PathVariable Long id,@Validated @RequestBody UsuarioRegistroDTO dto){
        return ResponseEntity.ok(usuarioService.modificarUsuario(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarUsuario(@PathVariable Long id){
        usuarioService.eliminarUsuario(id);
        return  ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\dto\auth\AuthRequestDTO.java
---

``java

package com.benconsult.user_management_api.users.dto.auth;

public record AuthRequestDTO(
        String username,
        String password
) {}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\dto\auth\AuthResponseDTO.java
---

``java

package com.benconsult.user_management_api.users.dto.auth;

public record AuthResponseDTO(
        String token,
        String refreshToken
) {}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\dto\usuario\UsuarioRegistroDTO.java
---

``java

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


``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\dto\usuario\UsuarioResponseDTO.java
---

``java

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

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\entity\Usuario.java
---

``java

package com.benconsult.user_management_api.users.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50, nullable = false)
    private String username;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean estado;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate(){
        this.fechaCreacion = LocalDateTime.now();
        this.estado = true;
    }
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\exception\BadCredentialsException.java
---

``java

package com.benconsult.user_management_api.users.exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\exception\GlobalExceptionHandler.java
---

``java

package com.benconsult.user_management_api.users.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    // Maneja cuando un usuario no existe en el sistema
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {
        log.warn("Usuario no encontrado: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado", ex.getMessage());
    }

    // Maneja intentos de registro o actualización con datos duplicados
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        log.warn("Usuario duplicado: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Usuario ya registrado", ex.getMessage());
    }

    // Maneja errores relacionados con credenciales y contraseñas
    @ExceptionHandler({
            PasswordMismatchException.class,
            InvalidPasswordException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<?> handleCredentialErrors(RuntimeException ex) {
        log.warn("Error de autenticación: {}", ex.getMessage());
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Error de autenticación",
                ex.getMessage()
        );
    }

    // Maneja cualquier error no controlado del sistema
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        log.error("Error interno del servidor", ex);
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                "Ocurrió un error inesperado"
        );
    }

    // Construye la respuesta estándar de error para la API
    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String error,
            String message
    ) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }
}


``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\exception\InvalidPasswordException.java
---

``java

package com.benconsult.user_management_api.users.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\exception\PasswordMismatchException.java
---

``java

package com.benconsult.user_management_api.users.exception;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException(String message) {
        super(message);
    }
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\exception\UserAlreadyExistsException.java
---

``java

package com.benconsult.user_management_api.users.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\exception\UserNotFoundException.java
---

``java

package com.benconsult.user_management_api.users.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}


``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\mapper\UsuarioMapper.java
---

``java

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
    @Mapping(target = "estado", ignore = true)
    Usuario toEntity(UsuarioRegistroDTO dto);

    // ENTITY ➜ DTO
    UsuarioResponseDTO toDto(Usuario entity);

    List<UsuarioResponseDTO> toDtoList(List<Usuario> entities);
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\repository\UsuarioRepository.java
---

``java

package com.benconsult.user_management_api.users.repository;

import com.benconsult.user_management_api.users.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsernameOrEmail(String username, String email);
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\security\JwtRequestFilter.java
---

``java

package com.benconsult.user_management_api.users.security;

import com.benconsult.user_management_api.users.service.impl.JwtUtilServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private UserDetailsService userDetailsService;
    private JwtUtilServiceImpl jwtUtilService;

    public JwtRequestFilter(UserDetailsService userDetailsService, JwtUtilServiceImpl jwtUtilService) {
        this.userDetailsService = userDetailsService;
        this.jwtUtilService = jwtUtilService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            String username = jwtUtilService.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtUtilService.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\security\SecurityConfig.java
---

``java

package com.benconsult.user_management_api.users.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/users").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return  authenticationConfiguration.getAuthenticationManager();
    }

}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\service\impl\JwtUtilServiceImpl.java
---

``java

package com.benconsult.user_management_api.users.service.impl;

import com.benconsult.user_management_api.users.service.JwtUtilService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
public class JwtUtilServiceImpl implements JwtUtilService {

    private static final String JWT_SECRET_KEY = "TExBVkVfTVVZX1NFQ1JFVEzE3Zmxu7BSGSJx72BSBXM";
    private static final long JWT_TIME_VALIDITY = 1000 * 60  * 15;
    private static final long JWT_TIME_REFRESH_VALIDATE = 1000 * 60  * 60 * 24;

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + JWT_TIME_VALIDITY)
                )
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + JWT_TIME_REFRESH_VALIDATE)
                )
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        return extractClaim(token, Claims::getSubject).equals(userDetails.getUsername())
                && !extractClaim(token, Claims::getExpiration).before(new Date());
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET_KEY).build().parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\service\impl\UserDetailsServiceImpl.java
---

``java

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

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\service\impl\UsuarioServiceImpl.java
---

``java

package com.benconsult.user_management_api.users.service.impl;

import com.benconsult.user_management_api.users.dto.usuario.UsuarioRegistroDTO;
import com.benconsult.user_management_api.users.dto.usuario.UsuarioResponseDTO;
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

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UsuarioResponseDTO registrarUsuario(UsuarioRegistroDTO dto) {
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setPassword(passwordEncoder.encode(dto.password()));

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(usuarioGuardado);
    }

    @Override
    public UsuarioResponseDTO modificarUsuario(Long id, UsuarioRegistroDTO dto) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: "+id));
        usuarioExistente.setUsername(dto.username());
        usuarioExistente.setEmail(dto.email());
        usuarioExistente.setPassword(dto.password());
        usuarioExistente.setEstado(dto.estado());

        if(dto.password() != null && !dto.password().isEmpty()){
            usuarioExistente.setPassword(passwordEncoder.encode(dto.password()));
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

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\service\JwtUtilService.java
---

``java

package com.benconsult.user_management_api.users.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtUtilService {
    String generateToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    boolean validateToken(String token, UserDetails userDetails);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    String extractUsername(String token);
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\service\UsuarioService.java
---

``java

package com.benconsult.user_management_api.users.service;

import com.benconsult.user_management_api.users.dto.usuario.UsuarioRegistroDTO;
import com.benconsult.user_management_api.users.dto.usuario.UsuarioResponseDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDTO registrarUsuario(UsuarioRegistroDTO dto);

    UsuarioResponseDTO modificarUsuario(Long id, UsuarioRegistroDTO dto);

    UsuarioResponseDTO obtenerUsuarioPorId(Long id);

    List<UsuarioResponseDTO> listarUsuarios();

    void eliminarUsuario(Long id);

    @SpringBootApplication
    class UserManagementApiApplication {

        public static void main(String[] args) {
            SpringApplication.run(UserManagementApiApplication.class, args);
        }

    }
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\main\java\com\benconsult\user_management_api\users\UserManagementApiApplication.java
---

``java

package com.benconsult.user_management_api.users;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserManagementApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserManagementApiApplication.class, args);
    }
}

``

---
## C:\DevProjects\prueba-tecnica-benconsult\user-management-api-backend\src\test\java\com\benconsult\user_management_api\UserManagementApiApplicationTests.java
---

``java

package com.benconsult.user_management_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserManagementApiApplicationTests {

	@Test
	void contextLoads() {
	}

}

``

