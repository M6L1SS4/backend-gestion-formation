package esta.bf.sir.repository;

import esta.bf.sir.model.audit.SecurityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SecurityLogRepository extends JpaRepository<SecurityLog, Long> {

    List<SecurityLog> findAllByOrderByDateEvenementDesc();

    List<SecurityLog> findByTypeEvenementOrderByDateEvenementDesc(SecurityLog.TypeEvenement typeEvenement);

    List<SecurityLog> findByUsernameOrderByDateEvenementDesc(String username);

    List<SecurityLog> findByAdresseIpOrderByDateEvenementDesc(String adresseIp);

    Long countByUsernameAndTypeEvenementAndDateEvenementAfter(String username,
                                                              SecurityLog.TypeEvenement typeEvenement, LocalDateTime dateEvenement);
}
