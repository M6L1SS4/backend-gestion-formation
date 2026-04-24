package esta.bf.sir.service;

import esta.bf.sir.model.Session;
import esta.bf.sir.model.SessionLog;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.model.enums.TypeActionSession;
import esta.bf.sir.repository.SessionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SessionLogService {

    private final SessionLogRepository sessionLogRepository;

    public void log(Session session,
                    Utilisateur utilisateur,
                    TypeActionSession action,
                    String details) {
        SessionLog log = new SessionLog();
        log.setSession(session);
        log.setUtilisateur(utilisateur);
        log.setAction(action);
        log.setDetails(details);
        log.setDateAction(LocalDateTime.now());
        sessionLogRepository.save(log);
    }
}
