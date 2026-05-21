package esta.bf.sir.controller.admin;

import esta.bf.sir.dto.CreateSondageRequest;
import esta.bf.sir.dto.UpdateSondageRequest;
import esta.bf.sir.model.OptionSondage;
import esta.bf.sir.model.QuestionSondage;
import esta.bf.sir.model.Sondage;
import esta.bf.sir.model.enums.StatutSondage;
import esta.bf.sir.service.SondageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/sondages")
@PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_FORMATION')")
@RequiredArgsConstructor
public class SondageAdminController {

    private final SondageService sondageService;

    @GetMapping
    public ResponseEntity<List<Sondage>> getAll() {
        return ResponseEntity.ok(sondageService.getAllSondages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sondage> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sondageService.getSondageById(id));
    }

    @PostMapping
    public ResponseEntity<Sondage> create(@RequestBody CreateSondageRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sondageService.createSondage(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sondage> update(
            @PathVariable Long id,
            @RequestBody UpdateSondageRequest request) {
        return ResponseEntity.ok(sondageService.updateSondage(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sondageService.deleteSondage(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<Sondage> changerStatut(@PathVariable Long id,
                                                 @RequestParam StatutSondage statut) {
        return ResponseEntity.ok(sondageService.changerStatut(id, statut));
    }

    @PostMapping("/{sondageId}/questions")
    public ResponseEntity<QuestionSondage> ajouterQuestion(
            @PathVariable Long sondageId,
            @RequestBody QuestionSondage question) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sondageService.ajouterQuestion(sondageId, question));
    }

    @PutMapping("/{sondageId}/questions/{questionId}")
    public ResponseEntity<QuestionSondage> updateQuestion(
            @PathVariable Long questionId,
            @RequestBody QuestionSondage question) {
        return ResponseEntity.ok(sondageService.updateQuestion(questionId, question));
    }

    @PostMapping("/{sondageId}/questions/{questionId}/options")
    public ResponseEntity<OptionSondage> ajouterOption(
            @PathVariable Long questionId,
            @RequestBody OptionSondage option) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sondageService.ajouterOption(questionId, option));
    }

    @DeleteMapping("/{sondageId}/questions/{questionId}")
    public ResponseEntity<Void> supprimerQuestion(@PathVariable Long questionId) {
        sondageService.supprimerQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{sondageId}/questions/{questionId}/options/{optionId}")
    public ResponseEntity<Void> supprimerOption(@PathVariable Long optionId) {
        sondageService.supprimerOption(optionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{sondageId}/rapport")
    public ResponseEntity<Map<String, Object>> rapport(@PathVariable Long sondageId) {
        return ResponseEntity.ok(sondageService.genererRapport(sondageId));
    }
}

