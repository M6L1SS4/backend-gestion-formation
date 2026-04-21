package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Question extends BaseEntity {

    private String texte;
    private String type;
    private Integer points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;
}
