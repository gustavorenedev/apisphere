package br.com.fiap.apisphere.auth;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.fiap.apisphere.user.UserRepository;

@RestController
public class AuthController {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public Token login(@RequestBody Credentials credentials) {

        // Email não encontrado
        var user = userRepository.findByEmail(credentials.email())
                        .orElseThrow(() -> new RuntimeException("Access Denied"));

        // Senha não confere
        if (!passwordEncoder.matches(credentials.password(), user.getPassword()))
            throw new RuntimeException("Access Denied");

        var expiresAt = LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.ofHours(-3));
        Algorithm algorithm = Algorithm.HMAC256("assinatura");
        String token = JWT.create()
                        .withIssuer("sphere")
                        .withSubject(credentials.email())
                        .withClaim("role", "admin")
                        .withExpiresAt(expiresAt)
                        .sign(algorithm);
        return new Token(token);
    }

}
