package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;

// model/ReponseSondage.java
@Entity
@Table(name = "reponses_sondages")
public class ReponseSondage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "sondage_id", nullable = false)
    private Sondage sondage;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionSondage question;

    // null si sondage anonyme
    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = true)
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = true)
    private OptionSondage optionChoisie;

    @Column(columnDefinition = "TEXT")
    private String reponseLibre;
}
