package esta.bf.sir.controller.auth;

import esta.bf.sir.dto.auth.*;
import esta.bf.sir.model.RefreshToken;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.security.JwtUtil;
import esta.bf.sir.service.AuthService;
import esta.bf.sir.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request, authenticationManager));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@RequestBody RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenService.valider(request.getRefreshToken());
        String newAccessToken = jwtUtil.generateAccessToken(refreshToken.getUtilisateur());

        return ResponseEntity.ok(RefreshResponse.builder()
                .accessToken(newAccessToken)
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal Utilisateur utilisateur) {
        refreshTokenService.revoquer(utilisateur.getId());
        return ResponseEntity.noContent().build();
    }
}
