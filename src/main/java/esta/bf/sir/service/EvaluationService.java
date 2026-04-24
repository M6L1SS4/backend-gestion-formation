package esta.bf.sir.service;

import esta.bf.sir.model.*;
import esta.bf.sir.model.enums.StatutEvaluation;
import esta.bf.sir.model.enums.TypeQuestion;
import esta.bf.sir.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final QuestionRepository questionRepository;
    private final ChoixReponseRepository choixReponseRepository;
    private final ResultatEvaluationRepository resultatRepository;
    private final ReponsesCandidatRepository reponseCandidatRepository;
    private final SessionRepository sessionRepository;

    // ── CRUD évaluation ───────────────────────────────────────────────────────

    public List<Evaluation> getEvaluationsBySession(Long sessionId){
        return evaluationRepository.findBySession_Id(sessionId);
    }

    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    public Evaluation getEvaluationById(Long id) {
        return evaluationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evaluation introuvable : " + id));
    }

    public Evaluation createEvaluation(Evaluation evaluation) {
        if (!evaluation.getDateDebut().isBefore(evaluation.getDateFin())) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }
        evaluation.setStatut(StatutEvaluation.PLANIFIEE);
        return evaluationRepository.save(evaluation);
    }

    public Evaluation updateEvaluation(Long id, Evaluation updated) {
        Evaluation evaluation = getEvaluationById(id);
        if (evaluation.getStatut() != StatutEvaluation.PLANIFIEE) {
            throw new IllegalStateException("Seule une évaluation planifiée peut être modifiée");
        }
        evaluation.setTitre(updated.getTitre());
        evaluation.setDescription(updated.getDescription());
        evaluation.setDateDebut(updated.getDateDebut());
        evaluation.setDateFin(updated.getDateFin());
        evaluation.setDureeMinutes(updated.getDureeMinutes());
        evaluation.setNoteMaximale(updated.getNoteMaximale());
        evaluation.setSeuilReussite(updated.getSeuilReussite());
        return evaluationRepository.save(evaluation);
    }

    public void deleteEvaluation(Long id) {
        Evaluation evaluation = getEvaluationById(id);
        if (evaluation.getStatut() == StatutEvaluation.EN_COURS) {
            throw new IllegalStateException("Impossible de supprimer une évaluation en cours");
        }
        evaluationRepository.delete(evaluation);
    }

    public Evaluation changerStatut(Long id, StatutEvaluation nouveauStatut) {
        Evaluation evaluation = getEvaluationById(id);
        evaluation.setStatut(nouveauStatut);
        return evaluationRepository.save(evaluation);
    }

    // ── Questions ─────────────────────────────────────────────────────────────

    public Question ajouterQuestion(Long evalId, Question question) {
        Evaluation evaluation = getEvaluationById(evalId);
        if (evaluation.getStatut() != StatutEvaluation.PLANIFIEE) {
            throw new IllegalStateException("Impossible d'ajouter une question à une évaluation non planifiée");
        }
        question.setEvaluation(evaluation);
        // Ordre automatique = dernier + 1
        int nextOrdre = evaluation.getQuestions().size() + 1;
        question.setOrdre(nextOrdre);
        return questionRepository.save(question);
    }

    public Question updateQuestion(Long evalId, Long questionId, Question updated) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question introuvable"));
        question.setEnonce(updated.getEnonce());
        question.setType(updated.getType());
        question.setPoints(updated.getPoints());
        question.setOrdre(updated.getOrdre());
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }

    // ── Choix de réponse ─────────────────────────────────────────────────────

    public ChoixReponse ajouterChoix(Long questionId, ChoixReponse choix) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question introuvable"));
        if (question.getType() == TypeQuestion.REPONSE_LIBRE) {
            throw new IllegalArgumentException("Une question à réponse libre n'a pas de choix");
        }
        choix.setQuestion(question);
        return choixReponseRepository.save(choix);
    }

    public void deleteChoix(Long choixId) {
        choixReponseRepository.deleteById(choixId);
    }

    // ── Soumission candidat ───────────────────────────────────────────────────

    // reponsesMap : Map<questionId, choixId ou null si réponse libre>
    // reponsesLibres : Map<questionId, texte>
    public ResultatEvaluation soumettre(Long evalId, Utilisateur candidat,
                                        Map<Long, Long> reponsesQcm,
                                        Map<Long, String> reponsesLibres) {
        Evaluation evaluation = getEvaluationById(evalId);

        if (evaluation.getStatut() != StatutEvaluation.EN_COURS) {
            throw new IllegalStateException("Cette évaluation n'est pas ouverte");
        }
        if (resultatRepository.existsByEvaluation_IdAndUtilisateur_Id(evalId, candidat.getId())) {
            throw new IllegalStateException("Vous avez déjà soumis cette évaluation");
        }

        ResultatEvaluation resultat = new ResultatEvaluation();
        resultat.setEvaluation(evaluation);
        resultat.setUtilisateur(candidat);
        resultat.setDateSoumission(LocalDateTime.now());

        double totalPoints = 0.0;

        for (Question question : evaluation.getQuestions()) {
            ReponseCandidat reponse = new ReponseCandidat();
            reponse.setResultat(resultat);
            reponse.setQuestion(question);

            if (question.getType() == TypeQuestion.REPONSE_LIBRE) {
                reponse.setReponseLibre(reponsesLibres.getOrDefault(question.getId(), ""));
                reponse.setPointsObtenus(0); // correction manuelle
            } else {
                Long choixId = reponsesQcm.get(question.getId());
                if (choixId != null) {
                    ChoixReponse choix = choixReponseRepository.findById(choixId)
                            .orElseThrow(() -> new EntityNotFoundException("Choix introuvable"));
                    reponse.setChoixSelectionne(choix);
                    double points = choix.isEstCorrect() ? question.getPoints() : 0.0;
                    reponse.setPointsObtenus(points);
                    totalPoints += points;
                }
            }
            resultat.getReponses().add(reponse);
        }

        resultat.setNoteObtenue(totalPoints);
        resultat.setReussi(totalPoints >= evaluation.getSeuilReussite());
        return resultatRepository.save(resultat);
    }

    public List<ResultatEvaluation> getResultatsByEvaluation(Long evalId) {
        return resultatRepository.findByEvaluation_Id(evalId);
    }

    public ResultatEvaluation getMonResultat(Long evalId, Long utilisateurId) {
        return resultatRepository.findByEvaluation_IdAndUtilisateur_Id(evalId, utilisateurId)
                .orElseThrow(() -> new EntityNotFoundException("Résultat introuvable"));
    }

    public List<ResultatEvaluation> getMesResultats(Long utilisateurId) {
        return resultatRepository.findByUtilisateur_Id(utilisateurId);
    }
}
