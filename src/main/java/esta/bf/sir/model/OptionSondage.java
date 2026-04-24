package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "options_sondages")
@Data
public class OptionSondage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionSondage question;

    @Column(nullable = false)
    private String texte;
}
