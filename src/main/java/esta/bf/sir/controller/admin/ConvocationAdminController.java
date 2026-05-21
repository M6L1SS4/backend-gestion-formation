package esta.bf.sir.controller.admin;

import esta.bf.sir.model.Convocation;
import esta.bf.sir.service.ConvocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// controller/admin/ConvocationAdminController.java
@RestController
@RequestMapping("/api/admin/sessions")
@PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_FORMATION')")
@RequiredArgsConstructor
public class ConvocationAdminController {

    private final ConvocationService convocationService;

    @GetMapping("/{sessionId}/convocations")
    public ResponseEntity<List<Convocation>> getConvocations(
            @PathVariable Long sessionId) {
        return ResponseEntity.ok(
                convocationService.getConvocationsBySession(sessionId)
        );
    }

    @PostMapping("/{sessionId}/convocations/candidats")
    public ResponseEntity<List<Convocation>> envoyerCandidats(
            @PathVariable Long sessionId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convocationService.envoyerConvocationsCandidats(sessionId));
    }

    @PostMapping("/{sessionId}/convocations/formateur")
    public ResponseEntity<Convocation> envoyerFormateur(
            @PathVariable Long sessionId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(convocationService.envoyerConvocationFormateur(sessionId));
    }
}
