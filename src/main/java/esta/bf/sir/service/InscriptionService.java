package esta.bf.sir.service;

import esta.bf.sir.model.Convocation;
import esta.bf.sir.model.Inscription;
import esta.bf.sir.model.Session;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.model.enums.StatutInscription;
import esta.bf.sir.model.enums.StatutSession;
import esta.bf.sir.model.enums.TypeActionSession;
import esta.bf.sir.repository.ConvocationRepository;
import esta.bf.sir.repository.InscriptionRepository;
import esta.bf.sir.repository.SessionRepository;
import esta.bf.sir.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final SessionRepository sessionRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ConvocationRepository convocationRepository;
    private final SessionLogService sessionLogService;

    public List<Inscription> getInscriptionsBySession(Long sessionId) {
        return inscriptionRepository.findBySession_Id(sessionId);
    }

    public Inscription getById(Long inscriptionId){
        return inscriptionRepository.findById(inscriptionId).orElseThrow(() -> new EntityNotFoundException(
                "Inscription introuvable"));
    }

    public List<Convocation> getMesConvocations(Long userId){
        return inscriptionRepository.findConvocationsByUtilisateur_Id(userId);
    }

    public List<Inscription> getMesInscriptions(Long utilisateurId) {
        return inscriptionRepository.findByUtilisateur_Id(utilisateurId);
    }

    public List<Inscription> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }

    public Inscription inscrire(Long sessionId, Long utilisateurId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session introuvable"));

        if (session.getStatut() != StatutSession.PLANIFIEE) {
            throw new IllegalStateException("Les inscriptions sont closes pour cette session");
        }
        if (inscriptionRepository.existsBySession_IdAndUtilisateur_Id(sessionId, utilisateurId)) {
            throw new IllegalStateException("Candidat déjà inscrit à cette session");
        }

        long nbInscrits = inscriptionRepository.countBySession_Id(sessionId);
        Utilisateur candidat = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        Inscription inscription = new Inscription();
        inscription.setSession(session);
        inscription.setUtilisateur(candidat);
        inscription.setDateInscription(LocalDateTime.now());

        // Capacité max atteinte → liste d'attente
        inscription.setStatut(nbInscrits >= session.getCapacite()
                ? StatutInscription.LISTE_ATTENTE
                : StatutInscription.EN_ATTENTE);

        Inscription saved = inscriptionRepository.save(inscription);
        sessionLogService.log(session, candidat, TypeActionSession.INSCRIPTION, null);
        return saved;
    }

    public void desinscrire(Long sessionId, Long utilisateurId) {
        Inscription inscription = inscriptionRepository
                .findBySession_IdAndUtilisateur_Id(sessionId, utilisateurId)
                .orElseThrow(() -> new EntityNotFoundException("Inscription introuvable"));

        if (inscription.getStatut() == StatutInscription.CONFIRMEE) {
            throw new IllegalStateException("Impossible de se désinscrire d'une inscription confirmée");
        }
        sessionLogService.log(inscription.getSession(), inscription.getUtilisateur(),
                TypeActionSession.DESINSCRIPTION, null);
        inscriptionRepository.delete(inscription);
    }

    public Inscription confirmer(Long inscriptionId) {
        Inscription inscription = inscriptionRepository.findById(inscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Inscription introuvable"));
        inscription.setStatut(StatutInscription.CONFIRMEE);
        return inscriptionRepository.save(inscription);
    }

    public Inscription annuler(Long inscriptionId) {
        Inscription inscription = inscriptionRepository.findById(inscriptionId)
                .orElseThrow(() -> new EntityNotFoundException("Inscription introuvable"));
        inscription.setStatut(StatutInscription.ANNULEE);
        return inscriptionRepository.save(inscription);
    }
}
