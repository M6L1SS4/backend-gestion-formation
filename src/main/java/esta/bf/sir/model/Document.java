package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import esta.bf.sir.model.enums.TypeDocument;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Data
public class Document extends BaseEntity {

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    @NotBlank(message = "L'UUID ne peut pas être vide")
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Format d'UUID invalide"
    )
    private String reference;      // numéro de référence unique du document

    private String cheminFichier;  // chemin vers le fichier stocké

    @Enumerated(EnumType.STRING)
    private TypeDocument type;     // SUPPORT_COURS, EXERCICE, ANNEXE...

    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;

}
