package esta.bf.sir.repository;

import esta.bf.sir.model.ReponseSondage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReponseSondageRepository extends JpaRepository<ReponseSondage, Long> {
    List<ReponseSondage> findBySondage_Id(Long sondageId);
    boolean existsBySondage_IdAndUtilisateur_Id(Long sondageId, Long utilisateurId);
}
