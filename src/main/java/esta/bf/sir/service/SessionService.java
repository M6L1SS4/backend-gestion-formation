package esta.bf.sir.service;

import esta.bf.sir.model.*;
import esta.bf.sir.model.enums.StatutSession;
import esta.bf.sir.model.enums.TypeActionSession;
import esta.bf.sir.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final CoursRepository coursRepository;
    private final FormateurInterneRepository formateurInterneRepository;
    private final FormateurExterneRepository formateurExterneRepository;
    private final InscriptionRepository inscriptionRepository;
    private final SessionLogService sessionLogService;

    // ── Admin ────────────────────────────────────────────────────────────────

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Session introuvable : " + id));
    }

    public List<Inscription> getInscritsBySession(Long id){
        return inscriptionRepository.findBySession_Id(id);
    }

    public Session createSession(Session session) {
        // Vérification que le cours existe et est actif
        Cours cours = coursRepository.findById(session.getCours().getId())
                .orElseThrow(() -> new EntityNotFoundException("Cours introuvable"));
        if (!cours.isActif()) {
            throw new IllegalStateException("Impossible de créer une session sur un cours inactif");
        }
        // Vérification cohérence des dates
        if (!session.getDateDebut().isBefore(session.getDateFin())) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }
        session.setCours(cours);
        session.setStatut(StatutSession.PLANIFIEE);
        return sessionRepository.save(session);
    }

    public Session updateSession(Long id, Session updated) {
        Session session = getSessionById(id);
        if (session.getStatut() == StatutSession.TERMINEE) {
            throw new IllegalStateException("Impossible de modifier une session terminée");
        }
        session.setDateDebut(updated.getDateDebut());
        session.setDateFin(updated.getDateFin());
        session.setLieu(updated.getLieu());
        session.setCapacite(updated.getCapacite());
        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        Session session = getSessionById(id);
        if (session.getStatut() == StatutSession.EN_COURS) {
            throw new IllegalStateException("Impossible de supprimer une session en cours");
        }
        sessionRepository.delete(session);
    }

    public Session changerStatut(Long id, StatutSession nouveauStatut) {
        Session session = getSessionById(id);
        validerTransitionStatut(session.getStatut(), nouveauStatut);
        session.setStatut(nouveauStatut);
        return sessionRepository.save(session);
    }

    public Session assignerFormateurInterne(Long sessionId, Long formateurId) {
        Session session = getSessionById(sessionId);
        FormateurInterne formateur = formateurInterneRepository.findById(formateurId)
                .orElseThrow(() -> new EntityNotFoundException("Formateur introuvable"));

        // Vérification que le formateur peut enseigner ce cours
        boolean peutEnseigner = formateur.getCoursEnseignables().stream()
                .anyMatch(c -> c.getId().equals(session.getCours().getId()));
        if (!peutEnseigner) {
            throw new IllegalArgumentException(
                    "Ce formateur ne peut pas enseigner : " + session.getCours().getTitre()
            );
        }
        session.setFormateurInterne(formateur);
        session.setFormateurExterne(null); // un seul formateur à la fois
        Session saved = sessionRepository.save(session);

        sessionLogService.log(session, null, TypeActionSession.FORMATEUR_ASSIGNE,
                "Formateur interne assigné : " + formateur.getUtilisateur().getNom());
        return saved;
    }

    public Session assignerFormateurExterne(Long sessionId, Long formateurId) {
        Session session = getSessionById(sessionId);
        FormateurExterne formateur = formateurExterneRepository.findById(formateurId)
                .orElseThrow(() -> new EntityNotFoundException("Formateur externe introuvable"));

        boolean peutEnseigner = formateur.getCoursEnseignables().stream()
                .anyMatch(c -> c.getId().equals(session.getCours().getId()));
        if (!peutEnseigner) {
            throw new IllegalArgumentException(
                    "Ce formateur ne peut pas enseigner : " + session.getCours().getTitre()
            );
        }
        session.setFormateurExterne(formateur);
        session.setFormateurInterne(null);
        Session saved = sessionRepository.save(session);

        sessionLogService.log(session, null, TypeActionSession.FORMATEUR_ASSIGNE,
                "Formateur externe assigné : " + formateur.getNom());
        return saved;
    }

    // ── Présences ─────────────────────────────────────────────────────────────

    // Map<inscriptionId, present>
    public void enregistrerPresences(Long sessionId, Map<Long, Boolean> presences,
                                     Utilisateur admin) {
        Session session = getSessionById(sessionId);
        if (session.getStatut() != StatutSession.TERMINEE) {
            throw new IllegalStateException("Les présences ne peuvent être validées que sur une session terminée");
        }
        presences.forEach((inscriptionId, present) -> {
            inscriptionRepository.findById(inscriptionId).ifPresent(inscription -> {
                inscription.setPresent(present);
                inscriptionRepository.save(inscription);

                TypeActionSession action = present
                        ? TypeActionSession.CONFIRMATION_PRESENCE
                        : TypeActionSession.ABSENCE;
                sessionLogService.log(session, inscription.getUtilisateur(), action, null);
            });
        });
    }

    // ── Formateur ─────────────────────────────────────────────────────────────

    public List<Session> getSessionsFormateur(Long formateurId) {
        return sessionRepository.findByFormateurInterne_Id(formateurId);
    }

    public Session retirerFormateur(Long id){
        return sessionRepository.deleteByFormateurExterne_Id(id);
    }

    // ── Candidat ─────────────────────────────────────────────────────────────

    public List<Session> getSessionsDisponibles() {
        return sessionRepository.findByStatut(StatutSession.PLANIFIEE);
    }

    // ── Privé ─────────────────────────────────────────────────────────────────

    private void validerTransitionStatut(StatutSession actuel, StatutSession nouveau) {
        Map<StatutSession, List<StatutSession>> transitions = Map.of(
                StatutSession.PLANIFIEE,  List.of(StatutSession.EN_COURS, StatutSession.ANNULEE),
                StatutSession.EN_COURS,   List.of(StatutSession.TERMINEE, StatutSession.ANNULEE),
                StatutSession.TERMINEE,   List.of(),
                StatutSession.ANNULEE,    List.of()
        );
        if (!transitions.get(actuel).contains(nouveau)) {
            throw new IllegalStateException(
                    "Transition invalide : " + actuel + " → " + nouveau
            );
        }
    }

}
