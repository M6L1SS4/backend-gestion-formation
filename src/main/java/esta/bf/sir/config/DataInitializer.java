package esta.bf.sir.config;

import esta.bf.sir.model.Formateur;
import esta.bf.sir.model.Participant;
import esta.bf.sir.model.Role;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.repository.FormateurRepository;
import esta.bf.sir.repository.ParticipantRepository;
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
    private FormateurRepository formateurRepository;
    
    @Autowired
    private ParticipantRepository participantRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Créer un administrateur
        if (!utilisateurRepository.existsByEmail("admin@formation.com")) {
            Utilisateur admin = new Utilisateur();
            admin.setEmail("admin@formation.com");
            admin.setMotDePasse(passwordEncoder.encode("admin123"));
            admin.setNom("Admin");
            admin.setPrenom("System");
            admin.setRole(Role.ADMIN);
            utilisateurRepository.save(admin);
        }
        
        // Créer un formateur
        if (!formateurRepository.existsByEmail("formateur@formation.com")) {
            Formateur formateur = new Formateur();
            formateur.setEmail("formateur@formation.com");
            formateur.setMotDePasse(passwordEncoder.encode("formateur123"));
            formateur.setNom("Dupont");
            formateur.setPrenom("Jean");
            formateur.setRole(Role.FORMATEUR);
            formateur.setSpecialite("Java Development");
            formateur.setTypeFormateur("Interne");
            formateur.setOrganisme("ESTA");
            formateurRepository.save(formateur);
        }
        
        // Créer un candidat
        if (!participantRepository.existsByEmail("candidat@formation.com")) {
            Participant participant = new Participant();
            participant.setEmail("candidat@formation.com");
            participant.setMotDePasse(passwordEncoder.encode("candidat123"));
            participant.setNom("Martin");
            participant.setPrenom("Sophie");
            participant.setRole(Role.CANDIDAT);
            participant.setMatricule(12345);
            participant.setService("IT");
            participant.setPoste("Développeur");
            participant.setTelephone(123456789);
            participantRepository.save(participant);
        }
        
        System.out.println("=== Données de test initialisées ===");
        System.out.println("Admin: admin@formation.com / admin123");
        System.out.println("Formateur: formateur@formation.com / formateur123");
        System.out.println("Candidat: candidat@formation.com / candidat123");
    }
}
