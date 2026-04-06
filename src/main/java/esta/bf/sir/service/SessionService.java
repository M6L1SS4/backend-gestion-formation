package esta.bf.sir.service;

import esta.bf.sir.model.Session;
import esta.bf.sir.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SessionService {
    
    @Autowired
    private SessionRepository sessionRepository;
    
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }
    
    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }
    
    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }
    
    public Session updateSession(Long id, Session sessionDetails) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        
        session.setLieu(sessionDetails.getLieu());
        session.setCapacite(sessionDetails.getCapacite());
        session.setStatut(sessionDetails.getStatut());
        session.setCours(sessionDetails.getCours());
        session.setFormateur(sessionDetails.getFormateur());
        
        return sessionRepository.save(session);
    }
    
    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }
}
