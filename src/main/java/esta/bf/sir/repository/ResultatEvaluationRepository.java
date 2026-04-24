package esta.bf.sir.repository;

import esta.bf.sir.model.ResultatEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResultatEvaluationRepository extends JpaRepository<ResultatEvaluation, Long> {
    List<ResultatEvaluation> findByEvaluation_Id(Long evalId);
    List<ResultatEvaluation> findByUtilisateur_Id(Long utilisateurId);
    Optional<ResultatEvaluation> findByEvaluation_IdAndUtilisateur_Id(Long evalId, Long utilisateurId);
    boolean existsByEvaluation_IdAndUtilisateur_Id(Long evalId, Long utilisateurId);
    int countByReussiTrue();
}
