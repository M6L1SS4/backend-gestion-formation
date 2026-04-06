package esta.bf.sir.repository;

import esta.bf.sir.model.Sondage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SondageRepository extends JpaRepository<Sondage, Long> {
}
