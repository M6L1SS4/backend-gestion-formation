package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "cours")
@Data
public class Cours extends BaseEntity {

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int dureeHeures;

    @ManyToOne
    @JoinColumn(name = "domaine_id", nullable = false)
    private Domaine domaine;

    // Filière : cours prérequis à suivre avant celui-ci
    // Ex: UML doit être suivi avant Java
    @ManyToMany
    @JoinTable(
            name = "cours_prerequis",
            joinColumns = @JoinColumn(name = "cours_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequis_id")
    )
    private List<Cours> prerequis = new ArrayList<>();

    // Documents associés à ce cours
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    private boolean actif = true;

}
