package esta.bf.sir.repository;

import esta.bf.sir.model.Sondage;
import esta.bf.sir.model.enums.StatutSondage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SondageRepository extends JpaRepository<Sondage, Long> {
    List<Sondage> findByStatut(StatutSondage statut);
    int countByStatut(StatutSondage statut);

}
