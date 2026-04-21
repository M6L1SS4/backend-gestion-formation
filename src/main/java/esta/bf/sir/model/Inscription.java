package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Inscription extends BaseEntity {

    private String statut;
    private Boolean presence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;
}
