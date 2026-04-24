package esta.bf.sir.repository;

import esta.bf.sir.model.FormateurInterne;
import esta.bf.sir.model.Inscription;
import esta.bf.sir.model.Session;
import esta.bf.sir.model.enums.StatutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByStatut(StatutSession statut);
    Optional<Session> findById(Long id);
    List<Session> findByCours_Id(Long coursId);
    List<Session> findByFormateurInterne_Id(Long formateurId);
    Session findByFormateurExterne_Id(Long formateurId);
    Session deleteByFormateurExterne_Id(Long formateurId);
    Session deleteByFormateurInterne_Id(Long formateurId);

    Long countByStatut(StatutSession statut);
}
