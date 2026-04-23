package esta.bf.sir.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.envers.RelationTargetAuditMode.NOT_AUDITED;

@Entity
@Table(name = "cours")
@Audited
public class Cours extends BaseEntity {

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    private int dureeHeures;

    @ManyToOne
    @JoinColumn(name = "domaine_id", nullable = false)
    @Audited(targetAuditMode = NOT_AUDITED)
    private Domaine domaine;

    // Filière : cours prérequis à suivre avant celui-ci
    // Ex: UML doit être suivi avant Java
    @ManyToMany
    @JoinTable(
            name = "cours_prerequis",
            joinColumns = @JoinColumn(name = "cours_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequis_id")
    )
    @Audited(targetAuditMode = NOT_AUDITED)
    private List<Cours> prerequis = new ArrayList<>();

    // Documents associés à ce cours
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

}
