package esta.bf.sir.controller;

import esta.bf.sir.dto.AuthRequest;
import esta.bf.sir.dto.AuthResponse;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.security.JwtUtil;
import esta.bf.sir.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(),
                authRequest.getMotDePasse()
            )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Utilisateur utilisateur = (Utilisateur) userDetails;

        String token = jwtUtil.generateToken(userDetails);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setRole(utilisateur.getRole().name());
        response.setEmail(utilisateur.getEmail());
        response.setNom(utilisateur.getNom());
        response.setPrenom(utilisateur.getPrenom());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody Utilisateur utilisateur) {
        Utilisateur savedUser = utilisateurService.createUtilisateur(utilisateur);

        String token = jwtUtil.generateToken(savedUser);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setRole(savedUser.getRole().name());
        response.setEmail(savedUser.getEmail());
        response.setNom(savedUser.getNom());
        response.setPrenom(savedUser.getPrenom());

        return ResponseEntity.ok(response);
    }
}
