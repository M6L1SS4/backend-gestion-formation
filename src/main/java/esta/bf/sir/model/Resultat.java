package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Resultat extends BaseEntity {

    private Double note;
    private String appreciation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;
}
