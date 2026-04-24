package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import esta.bf.sir.model.enums.StatutConvocation;
import jakarta.persistence.*;
import java.time.LocalDateTime;


// model/Convocation.java
@Entity
@Table(name = "convocations")
public class Convocation extends BaseEntity {

    // Convocation pour un candidat inscrit
    @OneToOne
    @JoinColumn(name = "inscription_id", nullable = true)
    private Inscription inscription;

    // Convocation pour un formateur
    @ManyToOne
    @JoinColumn(name = "formateur_interne_id", nullable = true)
    private FormateurInterne formateurInterne;

    @ManyToOne
    @JoinColumn(name = "formateur_externe_id", nullable = true)
    private FormateurExterne formateurExterne;

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(nullable = false)
    private LocalDateTime dateEnvoi;

    @Enumerated(EnumType.STRING)
    private StatutConvocation statut = StatutConvocation.ENVOYEE;

    private String contenu;        // texte de la convocation
}
