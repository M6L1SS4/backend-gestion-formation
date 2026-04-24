package esta.bf.sir.repository;

import esta.bf.sir.model.FormateurInterne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormateurInterneRepository extends JpaRepository<FormateurInterne, Long> {
    Optional<FormateurInterne> findByUtilisateurId(Long id);
    Boolean existsByUtilisateur_Id(Long id);
    Optional<FormateurInterne> findByUtilisateur_Id(Long id);
}
