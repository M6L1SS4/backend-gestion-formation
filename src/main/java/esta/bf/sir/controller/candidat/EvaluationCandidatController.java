package esta.bf.sir.controller.candidat;

import esta.bf.sir.dto.SoumissionRequest;
import esta.bf.sir.model.Evaluation;
import esta.bf.sir.model.ResultatEvaluation;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.model.enums.StatutEvaluation;
import esta.bf.sir.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidat/evaluations")
@PreAuthorize("hasRole('CANDIDAT')")
@RequiredArgsConstructor
public class EvaluationCandidatController {

    private final EvaluationService evaluationService;

    @GetMapping
    public ResponseEntity<List<Evaluation>> getDisponibles() {
        return ResponseEntity.ok(
                evaluationService.getAllEvaluations().stream()
                        .filter(e -> e.getStatut() == StatutEvaluation.EN_COURS)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getById(@PathVariable Long id,
                                              @AuthenticationPrincipal Utilisateur candidat) {
        Evaluation evaluation = evaluationService.getEvaluationById(id);
        // On masque les bonnes réponses avant d'envoyer
        evaluation.getQuestions().forEach(q ->
                q.getChoix().forEach(c -> c.setEstCorrect(false))
        );
        return ResponseEntity.ok(evaluation);
    }

    @PostMapping("/{id}/soumettre")
    public ResponseEntity<ResultatEvaluation> soumettre(
            @PathVariable Long id,
            @RequestBody SoumissionRequest request,
            @AuthenticationPrincipal Utilisateur candidat) {
        return ResponseEntity.ok(
                evaluationService.soumettre(id, candidat,
                        request.getReponsesQcm(),
                        request.getReponsesLibres())
        );
    }

    @GetMapping("/{id}/resultat")
    public ResponseEntity<ResultatEvaluation> getMonResultat(
            @PathVariable Long id,
            @AuthenticationPrincipal Utilisateur candidat) {
        return ResponseEntity.ok(evaluationService.getMonResultat(id, candidat.getId()));
    }

    @GetMapping("/mes-resultats")
    public ResponseEntity<List<ResultatEvaluation>> getMesResultats(
            @AuthenticationPrincipal Utilisateur candidat) {
        return ResponseEntity.ok(evaluationService.getMesResultats(candidat.getId()));
    }
}
