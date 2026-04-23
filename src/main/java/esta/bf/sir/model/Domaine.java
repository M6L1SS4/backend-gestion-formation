package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "domaines")
@Audited
public class Domaine extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String nom;           // "Informatique", "Management"...

    private String description;
}
