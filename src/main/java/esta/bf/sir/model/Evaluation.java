package esta.bf.sir.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import esta.bf.sir.model.base.BaseEntity;
import esta.bf.sir.model.enums.StatutEvaluation;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Audited
public class Evaluation extends BaseEntity {

    @Column(nullable = false)
    private String titre;

    private String description;

    @ManyToOne
    @JoinColumn(name = "domaine_id", nullable = false)
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Domaine domaine;

    // Une évaluation peut être liée à une session spécifique
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = true)
    private Session session;

    @Column(nullable = false)
    private LocalDateTime dateDebut;

    @Column(nullable = false)
    private LocalDateTime dateFin;

    private int dureeMinutes;      // temps imparti pour passer l'évaluation

    private double noteMaximale;   // le barème total

    private double seuilReussite;  // note minimale pour valider

    @Enumerated(EnumType.STRING)
    private StatutEvaluation statut = StatutEvaluation.PLANIFIEE;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResultatEvaluation> resultats = new ArrayList<>();
}
