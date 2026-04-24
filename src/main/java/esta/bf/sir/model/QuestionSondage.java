package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import esta.bf.sir.model.enums.TypeQuestion;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// model/QuestionSondage.java
@Entity
@Table(name = "questions_sondages")
@Data
public class QuestionSondage extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "sondage_id", nullable = false)
    private Sondage sondage;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String enonce;

    @Enumerated(EnumType.STRING)
    private TypeQuestion type;     // réutilise le même enum

    private int ordre;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionSondage> options = new ArrayList<>();
}
