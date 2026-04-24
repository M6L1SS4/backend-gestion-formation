package esta.bf.sir.repository;

import esta.bf.sir.model.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findBySession_Id(Long sessionId);
    List<Inscription> findByUtilisateur_Id(Long utilisateurId);
    boolean existsBySession_IdAndUtilisateur_Id(Long sessionId, Long utilisateurId);
    long countBySession_Id(Long sessionId);
    List<Inscription> findBySession_IdOrUtilisateur_Id(Long sessionId, Long utilisateurId);
    int countByPresentIsNotNull();
    int countByPresentTrue();
    Optional<Inscription> findBySession_IdAndUtilisateur_Id(Long sessionId, Long utilisateurId);
    List<Inscription> findAll();
}
