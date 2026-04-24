package esta.bf.sir.controller.candidat;

import esta.bf.sir.model.Convocation;
import esta.bf.sir.model.Inscription;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.service.InscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/candidat/inscriptions")
@PreAuthorize("hasRole('CANDIDAT')")
@RequiredArgsConstructor
public class InscriptionCandidatController {

    private final InscriptionService inscriptionService;

    @GetMapping
    public ResponseEntity<List<Inscription>> getMesInscriptions(
            @AuthenticationPrincipal Utilisateur candidat) {
        return ResponseEntity.ok(
                inscriptionService.getMesInscriptions(candidat.getId())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inscription> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal Utilisateur candidat) {
        Inscription inscription = inscriptionService.getById(id);
        // Un candidat ne peut voir que ses propres inscriptions
        if (!inscription.getUtilisateur().getId().equals(candidat.getId())) {
            throw new AccessDeniedException("Accès refusé");
        }
        return ResponseEntity.ok(inscription);
    }

    @GetMapping("/convocations")
    public ResponseEntity<List<Convocation>> getMesConvocations(
            @AuthenticationPrincipal Utilisateur candidat) {
        return ResponseEntity.ok(
                inscriptionService.getMesConvocations(candidat.getId())
        );
    }
}
