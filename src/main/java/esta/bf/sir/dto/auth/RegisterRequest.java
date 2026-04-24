package esta.bf.sir.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;
    // Pas de role — un register public crée toujours un CANDIDAT
    // L'admin crée les autres rôles via /api/admin/utilisateurs
}
