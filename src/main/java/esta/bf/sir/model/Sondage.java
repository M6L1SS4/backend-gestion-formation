package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import esta.bf.sir.model.enums.StatutSondage;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// model/Sondage.java
@Entity
@Table(name = "sondages")
@Data
public class Sondage extends BaseEntity {

    @Column(nullable = false)
    private String titre;

    private String description;

    @ManyToOne
    @JoinColumn(name = "domaine_id", nullable = false)
    private Domaine domaine;

    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;

    @Enumerated(EnumType.STRING)
    private StatutSondage statut = StatutSondage.PLANIFIE;

    private boolean anonyme;       // réponses anonymes ou non

    @OneToMany(mappedBy = "sondage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionSondage> questions = new ArrayList<>();
}
