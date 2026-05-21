package esta.bf.sir.service;

import esta.bf.sir.dto.CreateSondageRequest;
import esta.bf.sir.dto.ReponseSondageRequest;
import esta.bf.sir.dto.UpdateSondageRequest;
import esta.bf.sir.model.*;
import esta.bf.sir.model.enums.StatutSondage;
import esta.bf.sir.model.enums.TypeQuestion;
import esta.bf.sir.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SondageService {

    private final SondageRepository sondageRepository;
    private final QuestionSondageRepository questionSondageRepository;
    private final OptionSondageRepository optionSondageRepository;
    private final ReponseSondageRepository reponseSondageRepository;
    private final DomaineRepository domaineRepository;

    public List<Sondage> getAllSondages() {
        return sondageRepository.findAll();
    }

    public Sondage getSondageById(Long id) {
        return sondageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sondage introuvable : " + id));
    }

    public Sondage createSondage(CreateSondageRequest request) {
        // ← on récupère l'entité Domaine depuis l'id
        Domaine domaine = domaineRepository.findById(request.getDomaineId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Domaine introuvable : " + request.getDomaineId()
                ));

        Sondage sondage = new Sondage();
        sondage.setTitre(request.getTitre());
        sondage.setDescription(request.getDescription());
        sondage.setDomaine(domaine);          // ← objet complet, pas juste l'id
        sondage.setDateDebut(LocalDateTime.parse(
                request.getDateDebut(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        ));
        sondage.setDateFin(LocalDateTime.parse(
                request.getDateFin(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        ));
        sondage.setAnonyme(request.isAnonyme());
        sondage.setStatut(StatutSondage.PLANIFIE);
        return sondageRepository.save(sondage);
    }

    public Sondage updateSondage(Long id, UpdateSondageRequest request) {
        Sondage sondage = sondageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sondage introuvable : " + id));

        if (sondage.getStatut() != StatutSondage.PLANIFIE) {
            throw new IllegalStateException("Seul un sondage planifié peut être modifié");
        }

        sondage.setTitre(request.getTitre());
        sondage.setDescription(request.getDescription());
        sondage.setAnonyme(request.isAnonyme());
        sondage.setDateDebut(LocalDateTime.parse(
                request.getDateDebut(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        ));
        sondage.setDateFin(LocalDateTime.parse(
                request.getDateFin(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        ));

        if (request.getDomaineId() != null) {
            Domaine domaine = domaineRepository.findById(request.getDomaineId())
                    .orElseThrow(() -> new EntityNotFoundException("Domaine introuvable"));
            sondage.setDomaine(domaine);
        }

        return sondageRepository.save(sondage);
    }


    public void deleteSondage(Long id) {
        sondageRepository.deleteById(id);
    }


    public Sondage changerStatut(Long id, StatutSondage nouveauStatut) {
        Sondage sondage = getSondageById(id);
        sondage.setStatut(nouveauStatut);
        return sondageRepository.save(sondage);
    }

    // ── Questions et options ──────────────────────────────────────────────────

    public QuestionSondage ajouterQuestion(Long sondageId, QuestionSondage question) {
        Sondage sondage = getSondageById(sondageId);
        question.setSondage(sondage);
        question.setOrdre(sondage.getQuestions().size() + 1);
        return questionSondageRepository.save(question);
    }

    public void supprimerQuestion(Long questionId) {
        questionSondageRepository.deleteById(questionId);
    }

    public QuestionSondage updateQuestion(Long id, QuestionSondage question){
        QuestionSondage questionToUpdate = questionSondageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question introuvable"));
        questionToUpdate.setEnonce(question.getEnonce());
        questionToUpdate.setOrdre(question.getOrdre());
        return questionSondageRepository.save(questionToUpdate);
    }

    public void supprimerOption(Long optionId) {
        optionSondageRepository.deleteById(optionId);
    }

    public OptionSondage ajouterOption(Long questionId, OptionSondage option) {
        QuestionSondage question = questionSondageRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question introuvable"));
        option.setQuestion(question);
        return optionSondageRepository.save(option);
    }

    // ── Réponses candidat ─────────────────────────────────────────────────────

    public List<Sondage> getSondagesOuverts() {
        return sondageRepository.findByStatut(StatutSondage.EN_COURS);
    }

    public void repondre(Long sondageId, Utilisateur candidat,
                         List<ReponseSondageRequest> reponses) {
        Sondage sondage = getSondageById(sondageId);

        if (sondage.getStatut() != StatutSondage.EN_COURS) {
            throw new IllegalStateException("Ce sondage n'est pas ouvert");
        }
        // Un candidat ne peut répondre qu'une seule fois (sauf si anonyme)
        if (!sondage.isAnonyme() &&
                reponseSondageRepository.existsBySondage_IdAndUtilisateur_Id(
                        sondageId, candidat.getId())) {
            throw new IllegalStateException("Vous avez déjà répondu à ce sondage");
        }

        reponses.forEach(r -> {
            ReponseSondage reponse = new ReponseSondage();
            reponse.setSondage(sondage);
            reponse.setUtilisateur(sondage.isAnonyme() ? null : candidat);

            questionSondageRepository.findById(r.getQuestionId()).ifPresent(q -> {
                reponse.setQuestion(q);
                if (r.getOptionId() != null) {
                    optionSondageRepository.findById(r.getOptionId())
                            .ifPresent(reponse::setOptionChoisie);
                }
                reponse.setReponseLibre(r.getReponseLibre());
                reponseSondageRepository.save(reponse);
            });
        });
    }

    // ── Rapport agrégé ────────────────────────────────────────────────────────

    public Map<String, Object> genererRapport(Long sondageId) {
        Sondage sondage = getSondageById(sondageId);
        List<ReponseSondage> toutesReponses =
                reponseSondageRepository.findBySondage_Id(sondageId);

        Map<String, Object> rapport = new LinkedHashMap<>();
        rapport.put("sondage", sondage.getTitre());
        rapport.put("totalRepondants", toutesReponses.stream()
                .map(r -> r.getUtilisateur() != null ? r.getUtilisateur().getId() : null)
                .distinct().count());

        List<Map<String, Object>> questionsRapport = new ArrayList<>();
        for (QuestionSondage question : sondage.getQuestions()) {
            Map<String, Object> qRapport = new LinkedHashMap<>();
            qRapport.put("question", question.getEnonce());

            List<ReponseSondage> reponsesQuestion = toutesReponses.stream()
                    .filter(r -> r.getQuestion().getId().equals(question.getId()))
                    .toList();

            if (question.getType() != TypeQuestion.REPONSE_LIBRE) {
                // Comptage par option
                Map<String, Long> comptage = question.getOptions().stream()
                        .collect(Collectors.toMap(
                                OptionSondage::getTexte,
                                opt -> reponsesQuestion.stream()
                                        .filter(r -> r.getOptionChoisie() != null &&
                                                r.getOptionChoisie().getId().equals(opt.getId()))
                                        .count()
                        ));
                qRapport.put("resultats", comptage);
            } else {
                // Réponses libres brutes
                List<String> libres = reponsesQuestion.stream()
                        .map(ReponseSondage::getReponseLibre)
                        .filter(Objects::nonNull)
                        .toList();
                qRapport.put("reponsesLibres", libres);
            }
            questionsRapport.add(qRapport);
        }
        rapport.put("questions", questionsRapport);
        return rapport;
    }
}
