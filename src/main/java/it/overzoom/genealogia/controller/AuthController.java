package it.overzoom.genealogia.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.overzoom.genealogia.dto.AuthResponse;
import it.overzoom.genealogia.dto.LoginRequest;
import it.overzoom.genealogia.dto.RegisterRequest;
import it.overzoom.genealogia.model.User;
import it.overzoom.genealogia.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.register(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getFirstName(),
                    request.getLastName());

            // TODO: Generare JWT token reale
            String token = "fake-jwt-token-" + user.getId();

            AuthResponse response = new AuthResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    token);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return userService.login(request.getUsername(), request.getPassword())
                .map(user -> {
                    // TODO: Generare JWT token reale
                    String token = "fake-jwt-token-" + user.getId();

                    AuthResponse response = new AuthResponse(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            token);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401)
                        .body(new AuthResponse("Credenziali non valide")));
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String token) {
        // TODO: Estrarre userId dal JWT token
        // Per ora uso un fake parsing
        String userId = token.replace("Bearer ", "").replace("fake-jwt-token-", "");

        try {
            UUID id = UUID.fromString(userId);
            return userService.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}