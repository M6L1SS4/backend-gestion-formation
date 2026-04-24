package esta.bf.sir.controller.candidat;

import esta.bf.sir.dto.ReponseSondageRequest;
import esta.bf.sir.model.Sondage;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.service.SondageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidat/sondages")
@PreAuthorize("hasRole('CANDIDAT')")
@RequiredArgsConstructor
public class SondageCandidatController {

    private final SondageService sondageService;

    @GetMapping
    public ResponseEntity<List<Sondage>> getSondagesOuverts() {
        return ResponseEntity.ok(sondageService.getSondagesOuverts());
    }

    @PostMapping("/{id}/repondre")
    public ResponseEntity<Void> repondre(
            @PathVariable Long id,
            @RequestBody List<ReponseSondageRequest> reponses,
            @AuthenticationPrincipal Utilisateur candidat) {
        sondageService.repondre(id, candidat, reponses);
        return ResponseEntity.ok().build();
    }
}
