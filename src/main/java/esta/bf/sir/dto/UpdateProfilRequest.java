package esta.bf.sir.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfilRequest {
    private String nom;
    private String prenom;
    private String motDePasse; // nullable — ignoré si vide
}
