package esta.bf.sir.controller.admin;

import esta.bf.sir.model.ChoixReponse;
import esta.bf.sir.model.Evaluation;
import esta.bf.sir.model.Question;
import esta.bf.sir.model.ResultatEvaluation;
import esta.bf.sir.model.enums.StatutEvaluation;
import esta.bf.sir.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/evaluations")
@PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_FORMATION')")
@RequiredArgsConstructor
public class EvaluationAdminController {

    private final EvaluationService evaluationService;

    @GetMapping
    public ResponseEntity<List<Evaluation>> getAll() {
        return ResponseEntity.ok(evaluationService.getAllEvaluations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getById(@PathVariable Long id) {
        return ResponseEntity.ok(evaluationService.getEvaluationById(id));
    }

    @PostMapping
    public ResponseEntity<Evaluation> create(@RequestBody Evaluation evaluation) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(evaluationService.createEvaluation(evaluation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> update(@PathVariable Long id,
                                             @RequestBody Evaluation evaluation) {
        return ResponseEntity.ok(evaluationService.updateEvaluation(id, evaluation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<Evaluation> changerStatut(@PathVariable Long id,
                                                    @RequestParam StatutEvaluation statut) {
        return ResponseEntity.ok(evaluationService.changerStatut(id, statut));
    }

    @PostMapping("/{evalId}/questions")
    public ResponseEntity<Question> ajouterQuestion(@PathVariable Long evalId,
                                                    @RequestBody Question question) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(evaluationService.ajouterQuestion(evalId, question));
    }

    @PutMapping("/{evalId}/questions/{questionId}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long evalId,
                                                   @PathVariable Long questionId,
                                                   @RequestBody Question question) {
        return ResponseEntity.ok(evaluationService.updateQuestion(evalId, questionId, question));
    }

    @DeleteMapping("/{evalId}/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long evalId,
                                               @PathVariable Long questionId) {
        evaluationService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{evalId}/questions/{questionId}/choix")
    public ResponseEntity<ChoixReponse> ajouterChoix(@PathVariable Long questionId,
                                                     @RequestBody ChoixReponse choix) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(evaluationService.ajouterChoix(questionId, choix));
    }

    @DeleteMapping("/{evalId}/questions/{questionId}/choix/{choixId}")
    public ResponseEntity<Void> deleteChoix(@PathVariable Long choixId) {
        evaluationService.deleteChoix(choixId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{evalId}/resultats")
    public ResponseEntity<List<ResultatEvaluation>> getResultats(@PathVariable Long evalId) {
        return ResponseEntity.ok(evaluationService.getResultatsByEvaluation(evalId));
    }
}
