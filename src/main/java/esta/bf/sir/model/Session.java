package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import esta.bf.sir.model.enums.StatutSession;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Data
@Audited
public class Session extends BaseEntity {

    private String lieu;
    private Integer capacite;

    private LocalDateTime dateDebut;    // date + heure de début
    private LocalDateTime dateFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cours_id")
    private Cours cours;

    // Formateur interne — null si externe
    @ManyToOne
    @JoinColumn(name = "formateur_profil_id", nullable = true)
    @Audited(targetAuditMode = NOT_AUDITED)
    private FormateurInterne formateurInterne;

    // Formateur externe — null si interne
    @ManyToOne
    @JoinColumn(name = "formateur_externe_id", nullable = true)
    @Audited(targetAuditMode = NOT_AUDITED)
    private FormateurExterne formateurExterne;

    @Enumerated(EnumType.STRING)
    private StatutSession statut = StatutSession.PLANIFIEE;

    @Transient
    public Duration getDuree() {
        return Duration.between(dateDebut, dateFin);
    }

    // Contrainte métier vérifiée dans le service :
    // formateurInterne != null XOR formateurExterne != null
    @Transient
    public boolean hasFormateur() {
        return formateurInterne != null || formateurExterne != null;
    }


}


