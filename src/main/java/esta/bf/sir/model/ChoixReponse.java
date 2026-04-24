package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

// model/ChoixReponse.java
@Entity
@Table(name = "choix_reponses")
@Data
public class ChoixReponse extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false)
    private String texte;

    private boolean estCorrect;    // la bonne réponse
}
