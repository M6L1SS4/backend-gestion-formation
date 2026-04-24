package esta.bf.sir.controller.candidat;

import esta.bf.sir.dto.UpdateProfilRequest;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidat/profil")
@PreAuthorize("hasRole('CANDIDAT')")
@RequiredArgsConstructor
public class ProfilCandidatController {

    private final UtilisateurService utilisateurService;

    @GetMapping
    public ResponseEntity<Utilisateur> getProfil(
            @AuthenticationPrincipal Utilisateur utilisateur) {
        return ResponseEntity.ok(
                utilisateurService.getById(utilisateur.getId())
        );
    }

    @PutMapping
    public ResponseEntity<Utilisateur> updateProfil(
            @AuthenticationPrincipal Utilisateur utilisateur,
            @RequestBody UpdateProfilRequest request) {
        return ResponseEntity.ok(
                utilisateurService.updateProfil(utilisateur.getId(), request)
        );
    }
}
