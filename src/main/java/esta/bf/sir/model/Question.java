package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import esta.bf.sir.model.enums.TypeQuestion;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Data
@Audited
public class Question extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "evaluation_id", nullable = false)
    @Audited(targetAuditMode = NOT_AUDITED)
    private Evaluation evaluation;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enonce;

    @Enumerated(EnumType.STRING)
    private TypeQuestion type;     // QCM, VRAI_FAUX, REPONSE_LIBRE

    private double points;         // points attribués à cette question

    private int ordre;             // ordre d'affichage dans l'évaluation

    // Choix possibles pour QCM
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChoixReponse> choix = new ArrayList<>();

}
