package esta.bf.sir.controller.candidat;

import esta.bf.sir.model.Inscription;
import esta.bf.sir.model.Session;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.service.InscriptionService;
import esta.bf.sir.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidat/sessions")
@PreAuthorize("hasRole('CANDIDAT')")
@RequiredArgsConstructor
public class SessionCandidatController {

    private final SessionService sessionService;
    private final InscriptionService inscriptionService;

    @GetMapping
    public ResponseEntity<List<Session>> getSessionsDisponibles() {
        return ResponseEntity.ok(sessionService.getSessionsDisponibles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @PostMapping("/{id}/inscription")
    public ResponseEntity<Inscription> inscrire(
            @PathVariable Long id,
            @AuthenticationPrincipal Utilisateur candidat) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inscriptionService.inscrire(id, candidat.getId()));
    }

    @DeleteMapping("/{id}/inscription")
    public ResponseEntity<Void> desinscrire(
            @PathVariable Long id,
            @AuthenticationPrincipal Utilisateur candidat) {
        inscriptionService.desinscrire(id, candidat.getId());
        return ResponseEntity.noContent().build();
    }
}
