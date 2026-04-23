package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Table(name = "reponses_candidats")
@Audited
public class ReponseCandidat extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "resultat_id", nullable = false)
    private ResultatEvaluation resultat;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Question question;

    // Pour QCM : le choix sélectionné
    @ManyToOne
    @JoinColumn(name = "choix_id", nullable = true)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private ChoixReponse choixSelectionne;

    // Pour REPONSE_LIBRE : le texte saisi
    @Column(columnDefinition = "TEXT")
    private String reponseLibre;

    private double pointsObtenus;
}
