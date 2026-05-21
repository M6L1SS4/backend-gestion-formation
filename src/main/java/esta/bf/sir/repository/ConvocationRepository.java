package esta.bf.sir.repository;

import esta.bf.sir.model.Convocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConvocationRepository extends JpaRepository<Convocation, Long> {
    List<Convocation> findByInscription_Utilisateur_Id(Long utilisateurId);
    List<Convocation> findBySession_Id(Long sessionId);
    List<Convocation> findByFormateurInterne_Id(Long formateurId);
}
