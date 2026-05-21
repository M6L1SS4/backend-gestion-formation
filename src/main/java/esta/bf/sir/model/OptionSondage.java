package esta.bf.sir.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "options_sondages")
@Data
public class OptionSondage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    @JsonBackReference("question-option")
    private QuestionSondage question;

    @Column(nullable = false)
    private String texte;
}
