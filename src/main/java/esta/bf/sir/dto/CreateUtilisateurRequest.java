package esta.bf.sir.dto;

import esta.bf.sir.model.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUtilisateurRequest {
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    private Role role;
}
