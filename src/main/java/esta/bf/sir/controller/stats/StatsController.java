package esta.bf.sir.controller.stats;

import esta.bf.sir.model.enums.Role;
import esta.bf.sir.model.enums.StatutSession;
import esta.bf.sir.model.enums.StatutSondage;
import esta.bf.sir.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_FORMATION')")
@RequiredArgsConstructor
public class StatsController {

    private final UtilisateurRepository utilisateurRepository;
    private final FormateurInterneRepository formateurInterneRepository;
    private final FormateurExterneRepository formateurExterneRepository;
    private final SessionRepository sessionRepository;
    private final InscriptionRepository inscriptionRepository;
    private final EvaluationRepository evaluationRepository;
    private final ResultatEvaluationRepository resultatRepository;
    private final SondageRepository sondageRepository;
    private final CoursRepository coursRepository;

    @GetMapping("/tableau-de-bord")
    public ResponseEntity<Map<String, Object>> tableauDeBord() {
        Map<String, Object> stats = new LinkedHashMap<>();

        // Utilisateurs
        stats.put("totalUtilisateurs", utilisateurRepository.count());
        stats.put("totalCandidats",
                utilisateurRepository.countByRole(Role.CANDIDAT));
        stats.put("totalFormateurs",
                formateurInterneRepository.count() + formateurExterneRepository.count());

        // Sessions
        stats.put("totalSessions", sessionRepository.count());
        stats.put("sessionsPlanifiees",
                sessionRepository.countByStatut(StatutSession.PLANIFIEE));
        stats.put("sessionsEnCours",
                sessionRepository.countByStatut(StatutSession.EN_COURS));
        stats.put("sessionsTerminees",
                sessionRepository.countByStatut(StatutSession.TERMINEE));

        // Inscriptions
        stats.put("totalInscriptions", inscriptionRepository.count());
        stats.put("tauxPresence", calculerTauxPresence());

        // Evaluations
        stats.put("totalEvaluations", evaluationRepository.count());
        stats.put("tauxReussite", calculerTauxReussite());

        // Sondages
        stats.put("totalSondages", sondageRepository.count());
        stats.put("sondagesOuverts",
                sondageRepository.countByStatut(StatutSondage.EN_COURS));

        // Cours
        stats.put("totalCours", coursRepository.count());
        stats.put("coursActifs", coursRepository.countByActifTrue());

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/sessions")
    public ResponseEntity<Map<String, Object>> statsSession() {
        Map<String, Object> stats = new LinkedHashMap<>();
        for (StatutSession statut : StatutSession.values()) {
            stats.put(statut.name(), sessionRepository.countByStatut(statut));
        }
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/evaluations/taux-reussite")
    public ResponseEntity<Map<String, Object>> tauxReussite() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("tauxReussite", calculerTauxReussite());
        stats.put("totalResultats", resultatRepository.count());
        stats.put("totalReussis", resultatRepository.countByReussiTrue());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/inscriptions/taux-presence")
    public ResponseEntity<Map<String, Object>> tauxPresence() {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("tauxPresence", calculerTauxPresence());
        return ResponseEntity.ok(stats);
    }

    // ── Privé ─────────────────────────────────────────────────────────────────

    private double calculerTauxReussite() {
        long total = resultatRepository.count();
        if (total == 0) return 0.0;
        long reussis = resultatRepository.countByReussiTrue();
        return Math.round((double) reussis / total * 100.0 * 10) / 10.0;
    }

    private double calculerTauxPresence() {
        long total = inscriptionRepository.countByPresentIsNotNull();
        if (total == 0) return 0.0;
        long presents = inscriptionRepository.countByPresentTrue();
        return Math.round((double) presents / total * 100.0 * 10) / 10.0;
    }
}
