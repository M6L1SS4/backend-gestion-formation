package esta.bf.sir.model;

import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Formateur extends Utilisateur {

    private String specialite;
    private String typeFormateur;
    private String organisme;
}
