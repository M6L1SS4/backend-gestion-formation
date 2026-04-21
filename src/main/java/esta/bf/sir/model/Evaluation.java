package esta.bf.sir.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Evaluation extends BaseEntity {

    private String titre;
    private Integer duree;
    private String typeEvaluation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    @JsonIgnore
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

    @JsonIgnore
    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Resultat> resultats;
}
