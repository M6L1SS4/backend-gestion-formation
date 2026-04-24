package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "options_sondages")
@Audited
@Data
public class OptionSondage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private QuestionSondage question;

    @Column(nullable = false)
    private String texte;
}
