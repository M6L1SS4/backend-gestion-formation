package esta.bf.sir.controller.admin;

import esta.bf.sir.model.FormateurExterne;
import esta.bf.sir.model.FormateurInterne;
import esta.bf.sir.service.FormateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/formateurs")
@PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_FORMATION')")
@RequiredArgsConstructor
public class FormateurAdminController {

    private final FormateurService formateurService;

    // ── Internes ──────────────────────────────────────────────────────────────

    @GetMapping("/internes")
    public ResponseEntity<List<FormateurInterne>> getAllInternes() {
        return ResponseEntity.ok(formateurService.getAllInternes());
    }

    @GetMapping("/internes/{id}")
    public ResponseEntity<FormateurInterne> getInterneById(@PathVariable Long id) {
        return ResponseEntity.ok(formateurService.getInterneById(id));
    }

    @PostMapping("/internes")
    public ResponseEntity<FormateurInterne> createInterne(
            @RequestParam Long utilisateurId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(formateurService.createInterne(utilisateurId));
    }

    @DeleteMapping("/internes/{id}")
    public ResponseEntity<Void> deleteInterne(@PathVariable Long id) {
        formateurService.deleteInterne(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/internes/{id}/cours/{coursId}")
    public ResponseEntity<FormateurInterne> assignerCours(
            @PathVariable Long id,
            @PathVariable Long coursId) {
        return ResponseEntity.ok(formateurService.assignerCours(id, coursId));
    }

    @DeleteMapping("/internes/{id}/cours/{coursId}")
    public ResponseEntity<FormateurInterne> retirerCours(
            @PathVariable Long id,
            @PathVariable Long coursId) {
        return ResponseEntity.ok(formateurService.retirerCours(id, coursId));
    }

    // ── Externes ──────────────────────────────────────────────────────────────

    @GetMapping("/externes")
    public ResponseEntity<List<FormateurExterne>> getAllExternes() {
        return ResponseEntity.ok(formateurService.getAllExternes());
    }

    @GetMapping("/externes/{id}")
    public ResponseEntity<FormateurExterne> getExterneById(@PathVariable Long id) {
        return ResponseEntity.ok(formateurService.getExterneById(id));
    }

    @PostMapping("/externes")
    public ResponseEntity<FormateurExterne> createExterne(
            @RequestBody FormateurExterne formateur) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(formateurService.createExterne(formateur));
    }

    @PutMapping("/externes/{id}")
    public ResponseEntity<FormateurExterne> updateExterne(
            @PathVariable Long id,
            @RequestBody FormateurExterne formateur) {
        return ResponseEntity.ok(formateurService.updateExterne(id, formateur));
    }

    @DeleteMapping("/externes/{id}")
    public ResponseEntity<Void> deleteExterne(@PathVariable Long id) {
        formateurService.deleteExterne(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/externes/{id}/cours/{coursId}")
    public ResponseEntity<FormateurExterne> assignerCoursExterne(
            @PathVariable Long id,
            @PathVariable Long coursId) {
        return ResponseEntity.ok(formateurService.assignerCoursExterne(id, coursId));
    }

    @DeleteMapping("/externes/{id}/cours/{coursId}")
    public ResponseEntity<FormateurExterne> retirerCoursExterne(
            @PathVariable Long id,
            @PathVariable Long coursId) {
        return ResponseEntity.ok(formateurService.retirerCoursExterne(id, coursId));
    }
}
