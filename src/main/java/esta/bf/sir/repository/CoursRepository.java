package esta.bf.sir.repository;

import esta.bf.sir.model.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Long> {
    long countByActifTrue();
    boolean existsByDomaine_Id(Long domaineId);
}
