package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import esta.bf.sir.model.enums.StatutInscription;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "inscriptions")
public class Inscription extends BaseEntity {
    // model/Inscription.java

        @ManyToOne
        @JoinColumn(name = "session_id", nullable = false)
        private Session session;

        @ManyToOne
        @JoinColumn(name = "utilisateur_id", nullable = false)
        private Utilisateur utilisateur;   // le candidat inscrit

        @Column(nullable = false)
        private LocalDateTime dateInscription;

        @Enumerated(EnumType.STRING)
        private StatutInscription statut = StatutInscription.EN_ATTENTE;

        // Présence réelle à la session — rempli après la session
        private Boolean present;

        // Lien vers la convocation générée
        @OneToOne(mappedBy = "inscription", cascade = CascadeType.ALL)
        private Convocation convocation;

}
