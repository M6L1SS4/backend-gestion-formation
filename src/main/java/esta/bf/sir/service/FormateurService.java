package esta.bf.sir.service;

import esta.bf.sir.model.Cours;
import esta.bf.sir.model.FormateurExterne;
import esta.bf.sir.model.FormateurInterne;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.model.enums.Role;
import esta.bf.sir.repository.CoursRepository;
import esta.bf.sir.repository.FormateurExterneRepository;
import esta.bf.sir.repository.FormateurInterneRepository;
import esta.bf.sir.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormateurService {

    private final FormateurInterneRepository formateurInterneRepository;
    private final FormateurExterneRepository formateurExterneRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final CoursRepository coursRepository;

    // ── Formateur interne ─────────────────────────────────────────────────────

    public List<FormateurInterne> getAllInternes() {
        return formateurInterneRepository.findAll();
    }

    public FormateurInterne getInterneById(Long id) {
        return formateurInterneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formateur interne introuvable : " + id));
    }

    public FormateurInterne createInterne(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        if (utilisateur.getRole() != Role.FORMATEUR) {
            throw new IllegalArgumentException("L'utilisateur doit avoir le rôle FORMATEUR");
        }
        if (formateurInterneRepository.existsByUtilisateur_Id(utilisateurId)) {
            throw new IllegalStateException("Un profil formateur existe déjà pour cet utilisateur");
        }
        FormateurInterne formateur = new FormateurInterne();
        formateur.setUtilisateur(utilisateur);
        return formateurInterneRepository.save(formateur);
    }

    public void deleteInterne(Long id) {
        formateurInterneRepository.deleteById(id);
    }

    public FormateurInterne assignerCours(Long formateurId, Long coursId) {
        FormateurInterne formateur = getInterneById(formateurId);
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new EntityNotFoundException("Cours introuvable"));

        boolean dejaAssigne = formateur.getCoursEnseignables().stream()
                .anyMatch(c -> c.getId().equals(coursId));
        if (dejaAssigne) {
            throw new IllegalStateException("Ce cours est déjà assigné à ce formateur");
        }
        formateur.getCoursEnseignables().add(cours);
        return formateurInterneRepository.save(formateur);
    }

    public FormateurInterne retirerCours(Long formateurId, Long coursId) {
        FormateurInterne formateur = getInterneById(formateurId);
        formateur.getCoursEnseignables()
                .removeIf(c -> c.getId().equals(coursId));
        return formateurInterneRepository.save(formateur);
    }

    // ── Formateur externe ─────────────────────────────────────────────────────

    public List<FormateurExterne> getAllExternes() {
        return formateurExterneRepository.findAll();
    }

    public FormateurExterne getExterneById(Long id) {
        return formateurExterneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Formateur externe introuvable : " + id));
    }

    public FormateurExterne createExterne(FormateurExterne formateur) {
        if (formateurExterneRepository.existsByEmail(formateur.getEmail())) {
            throw new IllegalStateException("Un formateur externe avec cet email existe déjà");
        }
        return formateurExterneRepository.save(formateur);
    }

    public FormateurExterne updateExterne(Long id, FormateurExterne updated) {
        FormateurExterne formateur = getExterneById(id);
        formateur.setNom(updated.getNom());
        formateur.setPrenom(updated.getPrenom());
        formateur.setEmail(updated.getEmail());
        formateur.setTelephone(updated.getTelephone());
        formateur.setOrganisme(updated.getOrganisme());
        return formateurExterneRepository.save(formateur);
    }

    public void deleteExterne(Long id) {
        formateurExterneRepository.deleteById(id);
    }

    public FormateurExterne assignerCoursExterne(Long formateurId, Long coursId) {
        FormateurExterne formateur = getExterneById(formateurId);
        Cours cours = coursRepository.findById(coursId)
                .orElseThrow(() -> new EntityNotFoundException("Cours introuvable"));
        formateur.getCoursEnseignables().add(cours);
        return formateurExterneRepository.save(formateur);
    }

    public FormateurExterne retirerCoursExterne(Long formateurId, Long coursId) {
        FormateurExterne formateur = getExterneById(formateurId);
        formateur.getCoursEnseignables().removeIf(c -> c.getId().equals(coursId));
        return formateurExterneRepository.save(formateur);
    }

    // ── Profil formateur connecté ─────────────────────────────────────────────

    public FormateurInterne getMonProfil(Long utilisateurId) {
        return formateurInterneRepository.findByUtilisateur_Id(utilisateurId)
                .orElseThrow(() -> new EntityNotFoundException("Profil formateur introuvable"));
    }
}
