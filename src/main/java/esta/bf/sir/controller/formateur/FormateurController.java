package esta.bf.sir.controller.formateur;

import esta.bf.sir.model.*;
import esta.bf.sir.service.EvaluationService;
import esta.bf.sir.service.FormateurService;
import esta.bf.sir.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/formateur")
@PreAuthorize("hasRole('FORMATEUR')")
@RequiredArgsConstructor
public class FormateurController {

    private final FormateurService formateurService;
    private final SessionService sessionService;
    private final EvaluationService evaluationService;

    @GetMapping("/profil")
    public ResponseEntity<FormateurInterne> getProfil(
            @AuthenticationPrincipal Utilisateur utilisateur) {
        return ResponseEntity.ok(formateurService.getMonProfil(utilisateur.getId()));
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<Session>> getMesSessions(
            @AuthenticationPrincipal Utilisateur utilisateur) {
        FormateurInterne profil = formateurService.getMonProfil(utilisateur.getId());
        return ResponseEntity.ok(sessionService.getSessionsFormateur(profil.getId()));
    }

    @GetMapping("/sessions/{id}")
    public ResponseEntity<Session> getSession(
            @PathVariable Long id,
            @AuthenticationPrincipal Utilisateur utilisateur) {
        Session session = sessionService.getSessionById(id);
        // Vérifie que la session lui appartient
        FormateurInterne profil = formateurService.getMonProfil(utilisateur.getId());
        if (session.getFormateurInterne() == null ||
                !session.getFormateurInterne().getId().equals(profil.getId())) {
            throw new AccessDeniedException("Cette session ne vous est pas assignée");
        }
        return ResponseEntity.ok(session);
    }

    @GetMapping("/sessions/{id}/inscrits")
    public ResponseEntity<List<Inscription>> getInscrits(
            @PathVariable Long id,
            @AuthenticationPrincipal Utilisateur utilisateur) {
        // Même vérification d'appartenance dans le service
        return ResponseEntity.ok(sessionService.getInscritsBySession(id));
    }

    @PatchMapping("/sessions/{id}/presences")
    public ResponseEntity<Void> validerPresences(
            @PathVariable Long id,
            @RequestBody Map<Long, Boolean> presences,
            @AuthenticationPrincipal Utilisateur utilisateur) {
        sessionService.enregistrerPresences(id, presences, utilisateur);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cours")
    public ResponseEntity<List<Cours>> getMesCours(
            @AuthenticationPrincipal Utilisateur utilisateur) {
        FormateurInterne profil = formateurService.getMonProfil(utilisateur.getId());
        return ResponseEntity.ok(profil.getCoursEnseignables());
    }

    @GetMapping("/evaluations")
    public ResponseEntity<List<Evaluation>> getMesEvaluations(
            @AuthenticationPrincipal Utilisateur utilisateur) {
        FormateurInterne profil = formateurService.getMonProfil(utilisateur.getId());
        List<Session> sessions = sessionService.getSessionsFormateur(profil.getId());
        List<Evaluation> evaluations = sessions.stream()
                .flatMap(s -> evaluationService.getEvaluationsBySession(s.getId()).stream())
                .toList();
        return ResponseEntity.ok(evaluations);
    }
}
