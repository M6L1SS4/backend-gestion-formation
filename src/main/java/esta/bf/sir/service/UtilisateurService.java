package esta.bf.sir.service;

import esta.bf.sir.dto.CreateUtilisateurRequest;
import esta.bf.sir.dto.UpdateProfilRequest;
import esta.bf.sir.dto.UpdateUtilisateurRequest;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.model.enums.Role;
import esta.bf.sir.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UtilisateurService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + email));
    }

    public List<Utilisateur> getAll() {
        return utilisateurRepository.findAll();
    }

    public Utilisateur getById(Long id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable : " + id));
    }

    public Utilisateur create(CreateUtilisateurRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email déjà utilisé");
        }
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        utilisateur.setEmail(request.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        utilisateur.setRole(request.getRole());
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur update(Long id, UpdateUtilisateurRequest request) {
        Utilisateur utilisateur = getById(id);
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        // L'email ne change pas — trop de risques sur la sécurité
        if (request.getMotDePasse() != null && !request.getMotDePasse().isBlank()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        }
        return utilisateurRepository.save(utilisateur);
    }

    public void delete(Long id) {
        Utilisateur utilisateur = getById(id);
        utilisateurRepository.delete(utilisateur);
    }

    public Utilisateur changerRole(Long id, Role role) {
        Utilisateur utilisateur = getById(id);
        utilisateur.setRole(role);
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur updateProfil(Long id, UpdateProfilRequest request) {
        Utilisateur utilisateur = getById(id);
        utilisateur.setNom(request.getNom());
        utilisateur.setPrenom(request.getPrenom());
        if (request.getMotDePasse() != null && !request.getMotDePasse().isBlank()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
        }
        return utilisateurRepository.save(utilisateur);
    }
}
