package esta.bf.sir.repository;

import esta.bf.sir.model.Evaluation;
import esta.bf.sir.model.enums.StatutEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findBySession_Id(Long sessionId);
    List<Evaluation> findByStatut(StatutEvaluation statut);
    List<Evaluation> getEvaluationsBySession_Id(Long sessionId);
}
