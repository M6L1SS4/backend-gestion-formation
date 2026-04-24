package esta.bf.sir.service;

import esta.bf.sir.dto.auth.AuthRequest;
import esta.bf.sir.dto.auth.AuthResponse;
import esta.bf.sir.dto.auth.RegisterRequest;
import esta.bf.sir.model.RefreshToken;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.model.enums.Role;
import esta.bf.sir.repository.UtilisateurRepository;
import esta.bf.sir.security.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthResponse register(RegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Cet email est déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setRole(Role.CANDIDAT); // register public = toujours CANDIDAT
        utilisateur.setActif(true);

        utilisateur = utilisateurRepository.save(utilisateur);

        String accessToken = jwtUtil.generateAccessToken(utilisateur);
        RefreshToken refreshToken = refreshTokenService.creerOuRenouveler(utilisateur.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .role(utilisateur.getRole().name())
                .email(utilisateur.getEmail())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .build();
    }

    public AuthResponse login(AuthRequest request,
                              AuthenticationManager authenticationManager) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getMotDePasse()
                )
        );

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        if (!utilisateur.isActif()) {
            throw new IllegalStateException("Compte désactivé — contactez l'administrateur");
        }

        String accessToken = jwtUtil.generateAccessToken(utilisateur);
        RefreshToken refreshToken = refreshTokenService.creerOuRenouveler(utilisateur.getId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .role(utilisateur.getRole().name())
                .email(utilisateur.getEmail())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .build();
    }
}
