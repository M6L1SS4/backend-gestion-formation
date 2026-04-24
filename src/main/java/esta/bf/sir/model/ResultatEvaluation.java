package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resultats_evaluations")
@Audited
@Data
public class ResultatEvaluation extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "evaluation_id", nullable = false)
    private Evaluation evaluation;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;   // le candidat

    private double noteObtenue;

    private boolean reussi;            // noteObtenue >= seuilReussite

    private LocalDateTime dateSoumission;

    // Réponses détaillées du candidat
    @OneToMany(mappedBy = "resultat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReponseCandidat> reponses = new ArrayList<>();
}
