package esta.bf.sir.repository;

import esta.bf.sir.model.Formateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormateurRepository extends JpaRepository<Formateur, Long> {
    boolean existsByEmail(String email);
}
