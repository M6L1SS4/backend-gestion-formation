package esta.bf.sir.repository;

import esta.bf.sir.model.FormateurExterne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormateurExterneRepository extends JpaRepository<FormateurExterne, Long> {
}
