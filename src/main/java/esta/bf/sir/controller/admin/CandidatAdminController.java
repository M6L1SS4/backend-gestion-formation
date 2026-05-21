package esta.bf.sir.controller.admin;

import esta.bf.sir.dto.response.CandidatDetailResponse;
import esta.bf.sir.model.Inscription;
import esta.bf.sir.model.ResultatEvaluation;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.model.enums.Role;
import esta.bf.sir.repository.InscriptionRepository;
import esta.bf.sir.repository.ReponseSondageRepository;
import esta.bf.sir.repository.ResultatEvaluationRepository;
import esta.bf.sir.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// controller/admin/CandidatAdminController.java
@RestController
@RequestMapping("/api/admin/candidats")
@PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_FORMATION')")
@RequiredArgsConstructor
public class CandidatAdminController {

    private final UtilisateurRepository utilisateurRepository;
    private final InscriptionRepository inscriptionRepository;
    private final ResultatEvaluationRepository resultatRepository;
    private final ReponseSondageRepository reponseSondageRepository;

    // Liste tous les candidats
    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAllCandidats() {
        return ResponseEntity.ok(
                utilisateurRepository.findByRole(Role.CANDIDAT)
        );
    }

    // Détail complet d'un candidat
    @GetMapping("/{id}")
    public ResponseEntity<CandidatDetailResponse> getCandidatDetail(
            @PathVariable Long id) {
        Utilisateur candidat = utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable"));

        if (candidat.getRole() != Role.CANDIDAT) {
            throw new IllegalArgumentException("Cet utilisateur n'est pas un candidat");
        }

        List<Inscription> inscriptions =
                inscriptionRepository.findByUtilisateur_Id(id);
        List<ResultatEvaluation> resultats =
                resultatRepository.findByUtilisateur_Id(id);

        CandidatDetailResponse response = new CandidatDetailResponse();
        response.setCandidat(candidat);
        response.setInscriptions(inscriptions);
        response.setResultats(resultats);

        return ResponseEntity.ok(response);
    }
}
