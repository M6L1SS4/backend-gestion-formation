package esta.bf.sir.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Question {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String texte;
    private String type;
    private Integer points;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;
}
