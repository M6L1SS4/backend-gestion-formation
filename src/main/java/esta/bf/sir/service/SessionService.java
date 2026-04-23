package esta.bf.sir.service;

import esta.bf.sir.model.FormateurExterne;
import esta.bf.sir.model.FormateurInterne;
import esta.bf.sir.model.Session;
import esta.bf.sir.repository.FormateurExterneRepository;
import esta.bf.sir.repository.FormateurInterneRepository;
import esta.bf.sir.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final FormateurInterneRepository formateurInterneRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, FormateurInterneRepository formateurInterneRepository) {
        this.sessionRepository = sessionRepository;
        this.formateurInterneRepository = formateurInterneRepository;
    }

    @Autowired
    FormateurExterneRepository formateurExterneRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    public Session assignerFormateur(Long sessionId,
                                     Long formateurInterneId,
                                     Long formateurExterneId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));

        if (formateurInterneId != null) {
            FormateurInterne formateurInterne = formateurInterneRepository.findById(formateurInterneId)
                    .orElseThrow(() -> new RuntimeException("Formateur interne non trouvé"));
            session.setFormateurInterne(formateurInterne);
        } else if (formateurExterneId != null) {
            FormateurExterne formateurExterne = formateurExterneRepository.findById(formateurExterneId)
                    .orElseThrow(() -> new RuntimeException("Formateur externe non trouvé"));
            session.setFormateurExterne(formateurExterne);
        } else {
            throw new IllegalArgumentException(
                    "Au moins un des ID de formateur doit être fourni"
            );
        }

        return sessionRepository.save(session);
    }


    public Session updateSession(Long id, Session sessionDetails) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));

        session.setLieu(sessionDetails.getLieu());
        session.setCapacite(sessionDetails.getCapacite());
        session.setStatut(sessionDetails.getStatut());
        session.setCours(sessionDetails.getCours());
        session.setFormateurInterne(sessionDetails.getFormateurInterne());

        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }
}
