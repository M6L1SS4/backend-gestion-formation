package esta.bf.sir.controller.candidat;

import esta.bf.sir.dto.SoumissionRequest;
import esta.bf.sir.dto.response.EvaluationResponse;
import esta.bf.sir.dto.response.ResultatEvaluationResponse;
import esta.bf.sir.model.Evaluation;
import esta.bf.sir.model.ResultatEvaluation;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.model.enums.StatutEvaluation;
import esta.bf.sir.model.map.EvaluationMapper;
import esta.bf.sir.model.map.ResultatEvaluationMapper;
import esta.bf.sir.repository.ResultatEvaluationRepository;
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
    private final ResultatEvaluationMapper resultatEvaluationMapper;
    private final ResultatEvaluationRepository resultatRepository;
    private final EvaluationMapper evaluationMapper;

    @GetMapping
    public ResponseEntity<List<EvaluationResponse>> getDisponibles() {

        return ResponseEntity.ok(
                evaluationService.getAllEvaluations().stream()
                        .filter(e -> e.getStatut() == StatutEvaluation.EN_COURS)
                        .map(evaluationMapper::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal Utilisateur candidat) {

        Evaluation evaluation = evaluationService.getEvaluationById(id);

        return ResponseEntity.ok(
                evaluationMapper.toResponse(evaluation)
        );
    }

    @PostMapping("/{id}/soumettre")
    public ResponseEntity<ResultatEvaluationResponse> soumettre(
            @PathVariable Long id,
            @RequestBody SoumissionRequest request,
            @AuthenticationPrincipal Utilisateur candidat) {

        ResultatEvaluation resultat = evaluationService.soumettre(
                id,
                candidat,
                request.getReponsesQcm(),
                request.getReponsesLibres()
        );

        return ResponseEntity.ok(
                resultatEvaluationMapper.toResponse(resultat)
        );
    }

    @GetMapping("/{id}/resultat")
    public ResponseEntity<ResultatEvaluationResponse> getMonResultat(
            @PathVariable Long id,
            @AuthenticationPrincipal Utilisateur candidat) {

        return ResponseEntity.ok(
                resultatEvaluationMapper.toResponse(
                        evaluationService.getMonResultat(id, candidat.getId())
                )
        );
    }

    @GetMapping("/mes-resultats")
    public ResponseEntity<List<ResultatEvaluationResponse>> getMesResultats(
            @AuthenticationPrincipal Utilisateur candidat) {

        return ResponseEntity.ok(
                evaluationService.getMesResultats(candidat.getId())
                        .stream()
                        .map(resultatEvaluationMapper::toResponse)
                        .toList()
        );
    }
}
