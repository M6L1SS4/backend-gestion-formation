package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "options_sondages")
@Audited
public class OptionSondage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionSondage question;

    @Column(nullable = false)
    private String texte;
}
