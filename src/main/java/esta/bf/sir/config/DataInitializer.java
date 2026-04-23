package esta.bf.sir.config;

import esta.bf.sir.model.FormateurInterne;
import esta.bf.sir.model.enums.Role;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.repository.FormateurInterneRepository;
import esta.bf.sir.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private FormateurInterneRepository formateurInterneRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // ── Admin ────────────────────────────────────────────────────────────
        if (!utilisateurRepository.existsByEmail("admin@formation.com")) {
            Utilisateur admin = new Utilisateur();
            admin.setEmail("admin@formation.com");
            admin.setMotDePasse(passwordEncoder.encode("admin123"));
            admin.setNom("Admin");
            admin.setPrenom("System");
            admin.setRole(Role.ADMIN);
            utilisateurRepository.save(admin);
        }

        // ── Formateur interne ─────────────────────────────────────────────────
        // 1. On crée d'abord le compte Utilisateur avec le rôle FORMATEUR
        // 2. On crée ensuite le FormateurInterne lié à cet utilisateur
        if (!utilisateurRepository.existsByEmail("formateur@formation.com")) {
            Utilisateur utilisateurFormateur = new Utilisateur();
            utilisateurFormateur.setEmail("formateur@formation.com");
            utilisateurFormateur.setMotDePasse(passwordEncoder.encode("formateur123"));
            utilisateurFormateur.setNom("Dupont");
            utilisateurFormateur.setPrenom("Jean");
            utilisateurFormateur.setRole(Role.FORMATEUR);
            utilisateurFormateur = utilisateurRepository.save(utilisateurFormateur);

            // Le profil formateur lié — sans cours pour l'instant
            FormateurInterne formateurInterne = new FormateurInterne();
            formateurInterne.setUtilisateur(utilisateurFormateur);
            formateurInterneRepository.save(formateurInterne);
        }

        // ── Candidat ──────────────────────────────────────────────────────────
        // Un candidat est simplement un Utilisateur avec role = CANDIDAT
        // Pas de FormateurInterne, pas d'entité séparée
        if (!utilisateurRepository.existsByEmail("candidat@formation.com")) {
            Utilisateur candidat = new Utilisateur();
            candidat.setEmail("candidat@formation.com");
            candidat.setMotDePasse(passwordEncoder.encode("candidat123"));
            candidat.setNom("Martin");
            candidat.setPrenom("Sophie");
            candidat.setRole(Role.CANDIDAT);
            utilisateurRepository.save(candidat);
        }

        System.out.println("=== Données de test initialisées ===");
        System.out.println("Admin     : admin@formation.com     / admin123");
        System.out.println("Formateur : formateur@formation.com / formateur123");
        System.out.println("Candidat  : candidat@formation.com  / candidat123");
    }
}
