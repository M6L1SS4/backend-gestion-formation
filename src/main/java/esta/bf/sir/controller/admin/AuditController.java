package esta.bf.sir.controller.admin;

import esta.bf.sir.model.audit.SecurityLog;
import esta.bf.sir.repository.SecurityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


// controller/AuditController.java
@RestController
@RequestMapping("/api/admin/audit")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AuditController {

    private final SecurityLogRepository securityLogRepository;
//    private final AuditReader auditReader; // Envers

    // Tous les logs de sécurité
    @GetMapping("/security")
    public List<SecurityLog> getLogs() {
        return securityLogRepository.findAllByOrderByDateEvenementDesc();
    }

    // Logs filtrés par type
    @GetMapping("/security/{type}")
    public List<SecurityLog> getLogsByType(
            @PathVariable SecurityLog.TypeEvenement type
    ) {
        return securityLogRepository.findByTypeEvenementOrderByDateEvenementDesc(type);
    }

    // Historique Envers d'une entité — ex: /audit/envers/Utilisateur/42
//    @GetMapping("/envers/{entite}/{id}")
//    public List<?> getHistoriqueEntite(
//            @PathVariable String entite,
//            @PathVariable Long id
//    ) {
//        try {
//            return auditReader.createQuery()
//                    .forRevisionsOfEntity(
//                            Class.forName(entite), false, true
//                    )
//                    .add(AuditEntity.id().eq(id))
//                    .getResultList();
//        } catch (ClassNotFoundException e) {
//            throw new IllegalArgumentException("Entity class not found: " + entite, e);
//        }
//    }
}
