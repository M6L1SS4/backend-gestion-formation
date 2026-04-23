package esta.bf.sir.service;

import esta.bf.sir.model.audit.SecurityLog;
import esta.bf.sir.repository.SecurityLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// service/SecurityLogService.java
@Service
@RequiredArgsConstructor
public class SecurityLogService {

    private final SecurityLogRepository securityLogRepository;
    private final HttpServletRequest request; // inject pour récupérer l'IP

    // Seuil : X échecs en Y minutes = alerte
    private static final int SEUIL_ECHECS   = 5;
    private static final int FENETRE_MINUTES = 10;

    public void log(SecurityLog.TypeEvenement type, String username, String details) {
        SecurityLog log = SecurityLog.builder()
                .typeEvenement(type)
                .username(username)
                .adresseIp(getIpClient())
                .details(details)
                .dateEvenement(LocalDateTime.now())
                .alerteEnvoyee(false)
                .build();

        securityLogRepository.save(log);

        // Vérifie si une alerte doit être déclenchée
        if (type == SecurityLog.TypeEvenement.LOGIN_ECHEC) {
            verifierBruteForce(username);
        }
    }

    private void verifierBruteForce(String username) {
        LocalDateTime depuis = LocalDateTime.now().minusMinutes(FENETRE_MINUTES);

        long nbEchecs = securityLogRepository
                .countByUsernameAndTypeEvenementAndDateEvenementAfter(
                        username,
                        SecurityLog.TypeEvenement.LOGIN_ECHEC,
                        depuis
                );

        if (nbEchecs >= SEUIL_ECHECS) {
            // TODO : notifier l'admin (email, WebSocket...)
            // Pour l'instant on log l'alerte
            System.out.println("ALERTE : Brute force détecté sur " + username);
        }
    }

    private String getIpClient() {
        String ip = request.getHeader("X-Forwarded-For");
        return ip != null ? ip : request.getRemoteAddr();
    }
}
