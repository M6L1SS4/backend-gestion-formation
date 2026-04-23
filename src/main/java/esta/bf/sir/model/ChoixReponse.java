package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

// model/ChoixReponse.java
@Entity
@Table(name = "choix_reponses")
@Audited
public class ChoixReponse extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @Audited(targetAuditMode = NOT_AUDITED)
    private Question question;

    @Column(nullable = false)
    private String texte;

    private boolean estCorrect;    // la bonne réponse
}
