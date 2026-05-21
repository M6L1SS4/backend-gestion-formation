package esta.bf.sir.controller.admin;

import esta.bf.sir.model.Inscription;
import esta.bf.sir.service.InscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// controller/admin/InscriptionAdminController.java
@RestController
@RequestMapping("/api/admin/inscriptions")
@PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_FORMATION')")
@RequiredArgsConstructor
public class InscriptionAdminController {

    private final InscriptionService inscriptionService;

    @GetMapping("/{id}")
    public ResponseEntity<Inscription> getById(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionService.getById(id));
    }

    @PostMapping("/sessions/{sessionId}")
    public ResponseEntity<Inscription> inscrire(
            @PathVariable Long sessionId,
            @RequestParam Long utilisateurId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inscriptionService.inscrire(sessionId, utilisateurId));
    }

    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<Inscription> confirmer(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionService.confirmer(id));
    }

    @PatchMapping("/{id}/annuler")
    public ResponseEntity<Inscription> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(inscriptionService.annuler(id));
    }
}
