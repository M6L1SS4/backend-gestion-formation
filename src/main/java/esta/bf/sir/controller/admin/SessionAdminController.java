package esta.bf.sir.controller.admin;

import esta.bf.sir.model.Inscription;
import esta.bf.sir.model.Session;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.model.enums.StatutSession;
import esta.bf.sir.service.InscriptionService;
import esta.bf.sir.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/sessions")
@PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_FORMATION')")
@RequiredArgsConstructor
public class SessionAdminController {

    private final SessionService sessionService;
    private final InscriptionService inscriptionService;

    @GetMapping
    public ResponseEntity<List<Session>> getAll() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @PostMapping
    public ResponseEntity<Session> create(@RequestBody Session session) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sessionService.createSession(session));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Session> update(@PathVariable Long id,
                                          @RequestBody Session session) {
        return ResponseEntity.ok(sessionService.updateSession(id, session));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<Session> changerStatut(@PathVariable Long id,
                                                 @RequestParam StatutSession statut) {
        return ResponseEntity.ok(sessionService.changerStatut(id, statut));
    }

    @PatchMapping("/{id}/formateur-interne/{formateurId}")
    public ResponseEntity<Session> assignerFormateurInterne(@PathVariable Long id,
                                                            @PathVariable Long formateurId) {
        return ResponseEntity.ok(sessionService.assignerFormateurInterne(id, formateurId));
    }

    @PatchMapping("/{id}/formateur-externe/{formateurId}")
    public ResponseEntity<Session> assignerFormateurExterne(@PathVariable Long id,
                                                            @PathVariable Long formateurId) {
        return ResponseEntity.ok(sessionService.assignerFormateurExterne(id, formateurId));
    }

    @DeleteMapping("/{id}/formateur")
    public ResponseEntity<Session> retirerFormateur(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.retirerFormateur(id));
    }

    @GetMapping("/{sessionId}/inscriptions")
    public ResponseEntity<List<Inscription>> getInscrits(@PathVariable Long sessionId) {
        return ResponseEntity.ok(inscriptionService.getInscriptionsBySession(sessionId));
    }

    @PatchMapping("/{sessionId}/presences")
    public ResponseEntity<Void> enregistrerPresences(
            @PathVariable Long sessionId,
            @RequestBody Map<Long, Boolean> presences,
            @AuthenticationPrincipal Utilisateur admin) {
        sessionService.enregistrerPresences(sessionId, presences, admin);
        return ResponseEntity.ok().build();
    }
}
