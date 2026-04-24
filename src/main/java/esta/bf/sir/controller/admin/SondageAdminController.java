package esta.bf.sir.controller.admin;

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
    public ResponseEntity<Sondage> create(@RequestBody Sondage sondage) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sondageService.createSondage(sondage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sondage> update(@PathVariable Long id,
                                          @RequestBody Sondage sondage) {
        return ResponseEntity.ok(sondageService.updateSondage(id, sondage));
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

    @PostMapping("/{sondageId}/questions/{questionId}/options")
    public ResponseEntity<OptionSondage> ajouterOption(
            @PathVariable Long questionId,
            @RequestBody OptionSondage option) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sondageService.ajouterOption(questionId, option));
    }

    @GetMapping("/{sondageId}/rapport")
    public ResponseEntity<Map<String, Object>> rapport(@PathVariable Long sondageId) {
        return ResponseEntity.ok(sondageService.genererRapport(sondageId));
    }
}

