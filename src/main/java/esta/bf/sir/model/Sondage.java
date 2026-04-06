package esta.bf.sir.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Sondage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titre;
    private String description;
}
