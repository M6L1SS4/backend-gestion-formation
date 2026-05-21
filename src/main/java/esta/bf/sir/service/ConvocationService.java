package esta.bf.sir.service;

import esta.bf.sir.model.*;
import esta.bf.sir.model.enums.StatutConvocation;
import esta.bf.sir.model.enums.StatutInscription;
import esta.bf.sir.model.enums.TypeActionSession;
import esta.bf.sir.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

// ConvocationService.java
@Service
@RequiredArgsConstructor
public class ConvocationService {

    private final ConvocationRepository convocationRepository;
    private final InscriptionRepository inscriptionRepository;
    private final SessionRepository sessionRepository;
    private final FormateurInterneRepository formateurInterneRepository;
    private final FormateurExterneRepository formateurExterneRepository;
    private final SessionLogService sessionLogService;

    // Envoie les convocations à tous les candidats confirmés d'une session
    public List<Convocation> envoyerConvocationsCandidats(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session introuvable"));

        List<Inscription> inscriptionsConfirmees = inscriptionRepository
                .findBySession_Id(sessionId)
                .stream()
                .filter(i -> i.getStatut() == StatutInscription.CONFIRMEE)
                .toList();

        if (inscriptionsConfirmees.isEmpty()) {
            throw new IllegalStateException(
                    "Aucun inscrit confirmé pour cette session"
            );
        }

        List<Convocation> convocations = inscriptionsConfirmees.stream()
                .map(inscription -> {
                    Convocation convocation = new Convocation();
                    convocation.setSession(session);
                    convocation.setInscription(inscription);
                    convocation.setDateEnvoi(LocalDateTime.now());
                    convocation.setStatut(StatutConvocation.ENVOYEE);
                    convocation.setContenu(genererContenuCandidat(inscription));
                    return convocationRepository.save(convocation);
                })
                .toList();

        // ── SessionLog ────────────────────────────────────────────────────
        sessionLogService.log(
                session,
                null,
                TypeActionSession.CONVOCATION_ENVOYEE,
                convocations.size() + " convocation(s) envoyée(s) aux candidats"
        );

        return convocations;
    }

    // Envoie la convocation au formateur de la session
    public Convocation envoyerConvocationFormateur(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Session introuvable"));

        if (session.getFormateurInterne() == null
                && session.getFormateurExterne() == null) {
            throw new IllegalStateException(
                    "Aucun formateur assigné à cette session"
            );
        }

        Convocation convocation = new Convocation();
        convocation.setSession(session);
        convocation.setDateEnvoi(LocalDateTime.now());
        convocation.setStatut(StatutConvocation.ENVOYEE);

        if (session.getFormateurInterne() != null) {
            convocation.setFormateurInterne(session.getFormateurInterne());
            convocation.setContenu(
                    genererContenuFormateurInterne(session)
            );
        } else {
            convocation.setFormateurExterne(session.getFormateurExterne());
            convocation.setContenu(
                    genererContenuFormateurExterne(session)
            );
        }

        Convocation saved = convocationRepository.save(convocation);

        sessionLogService.log(
                session,
                null,
                TypeActionSession.CONVOCATION_ENVOYEE,
                "Convocation envoyée au formateur"
        );

        return saved;
    }

    public List<Convocation> getConvocationsBySession(Long sessionId) {
        return convocationRepository.findBySession_Id(sessionId);
    }

    // ── Génération du contenu ─────────────────────────────────────────────

    private String genererContenuCandidat(Inscription inscription) {
        Session s = inscription.getSession();
        return String.format(
                "Madame/Monsieur %s %s,\n\n" +
                        "Vous êtes convoqué(e) à la session de formation suivante :\n\n" +
                        "Formation : %s\n" +
                        "Date de début : %s\n" +
                        "Date de fin : %s\n" +
                        "Lieu : %s\n\n" +
                        "Merci de vous présenter à l'heure indiquée.\n\n" +
                        "Cordialement,\nLe service formation OKI",
                inscription.getUtilisateur().getPrenom(),
                inscription.getUtilisateur().getNom(),
                s.getCours().getTitre(),
                s.getDateDebut().toLocalDate(),
                s.getDateFin().toLocalDate(),
                s.getLieu() != null ? s.getLieu() : "À définir"
        );
    }

    private String genererContenuFormateurInterne(Session session) {
        FormateurInterne f = session.getFormateurInterne();
        return String.format(
                "Madame/Monsieur %s %s,\n\n" +
                        "Vous êtes convoqué(e) en tant que formateur pour la session :\n\n" +
                        "Formation : %s\n" +
                        "Date de début : %s\n" +
                        "Date de fin : %s\n" +
                        "Lieu : %s\n\n" +
                        "Cordialement,\nLe service formation OKI",
                f.getUtilisateur().getPrenom(),
                f.getUtilisateur().getNom(),
                session.getCours().getTitre(),
                session.getDateDebut().toLocalDate(),
                session.getDateFin().toLocalDate(),
                session.getLieu() != null ? session.getLieu() : "À définir"
        );
    }

    private String genererContenuFormateurExterne(Session session) {
        FormateurExterne f = session.getFormateurExterne();
        return String.format(
                "Madame/Monsieur %s %s (%s),\n\n" +
                        "Vous êtes convoqué(e) en tant que formateur externe pour :\n\n" +
                        "Formation : %s\n" +
                        "Date de début : %s\n" +
                        "Date de fin : %s\n" +
                        "Lieu : %s\n\n" +
                        "Cordialement,\nLe service formation OKI",
                f.getPrenom(),
                f.getNom(),
                f.getOrganisme(),
                session.getCours().getTitre(),
                session.getDateDebut().toLocalDate(),
                session.getDateFin().toLocalDate(),
                session.getLieu() != null ? session.getLieu() : "À définir"
        );
    }
}
