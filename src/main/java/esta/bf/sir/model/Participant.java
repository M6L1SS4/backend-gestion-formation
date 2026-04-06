package esta.bf.sir.model;

import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Participant extends Utilisateur {

    private Integer matricule;
    private String service;
    private String poste;
    private Integer telephone;
}
