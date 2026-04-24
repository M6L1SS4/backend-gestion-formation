package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// FormateurExterne — pas de compte utilisateur
// Juste des informations de contact pour la convocation
@Entity
@Table(name = "formateurs_externes")
@Data
public class FormateurExterne extends BaseEntity {

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;

    @Column(nullable = false)
    private String organisme;      // l'entreprise externe

    @ManyToMany
    @JoinTable(
            name = "formateur_externe_cours",
            joinColumns = @JoinColumn(name = "formateur_externe_id"),
            inverseJoinColumns = @JoinColumn(name = "cours_id")
    )
    private List<Cours> coursEnseignables = new ArrayList<>();
}
