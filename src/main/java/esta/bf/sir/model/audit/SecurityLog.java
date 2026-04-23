package esta.bf.sir.model.audit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "security_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TypeEvenement typeEvenement;

    private String username;        // qui a tenté
    private String adresseIp;       // depuis où
    private String details;         // message d'erreur, ressource tentée...

    private LocalDateTime dateEvenement;

    private boolean alerteEnvoyee;

    public enum TypeEvenement {
        LOGIN_SUCCES,
        LOGIN_ECHEC,
        ACCES_REFUSE,        // 403
        TOKEN_EXPIRE,
        MODIFICATION_ENTITE,
        SUPPRESSION_ENTITE
    }
}
