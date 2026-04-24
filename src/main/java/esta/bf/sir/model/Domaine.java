package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "domaines")
@Data
public class Domaine extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String nom;           // "Informatique", "Management"...

    private String description;
}
