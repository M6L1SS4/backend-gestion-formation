package esta.bf.sir.repository;

import esta.bf.sir.model.Domaine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomaineRepository extends JpaRepository<Domaine, Long> {
    boolean existsByNom(String nom);
}
