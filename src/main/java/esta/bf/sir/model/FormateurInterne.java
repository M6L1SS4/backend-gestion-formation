package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class FormateurInterne extends BaseEntity {

    // Lien 1-1 vers le compte utilisateur — jamais null
    @OneToOne
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    // Cours que ce formateur peut enseigner
    @ManyToMany
    @JoinTable(
            name = "formateur_cours",
            joinColumns = @JoinColumn(name = "formateur_profil_id"),
            inverseJoinColumns = @JoinColumn(name = "cours_id")
    )
    private List<Cours> coursEnseignables = new ArrayList<>();
}
