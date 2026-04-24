package esta.bf.sir.service;

import esta.bf.sir.dto.CreateCoursRequest;
import esta.bf.sir.dto.CreateDocumentRequest;
import esta.bf.sir.dto.UpdateCoursRequest;
import esta.bf.sir.model.Cours;
import esta.bf.sir.model.Document;
import esta.bf.sir.model.Domaine;
import esta.bf.sir.repository.CoursRepository;
import esta.bf.sir.repository.DocumentRepository;
import esta.bf.sir.repository.DomaineRepository;
import esta.bf.sir.repository.SessionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoursService {

    private final CoursRepository coursRepository;
    private final DomaineRepository domaineRepository;
    private final DocumentRepository documentRepository;
    private final SessionRepository sessionRepository;

    public List<Cours> getAll() {
        return coursRepository.findAll();
    }

    public Cours getById(Long id) {
        return coursRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cours introuvable : " + id));
    }

    public Cours create(CreateCoursRequest request) {
        Domaine domaine = domaineRepository.findById(request.getDomaineId())
                .orElseThrow(() -> new EntityNotFoundException("Domaine introuvable"));

        Cours cours = new Cours();
        cours.setTitre(request.getTitre());
        cours.setDescription(request.getDescription());
        cours.setDureeHeures(request.getDureeHeures());
        cours.setDomaine(domaine);
        cours.setActif(true);
        return coursRepository.save(cours);
    }

    public Cours update(Long id, UpdateCoursRequest request) {
        Cours cours = getById(id);
        cours.setTitre(request.getTitre());
        cours.setDescription(request.getDescription());
        cours.setDureeHeures(request.getDureeHeures());

        if (request.getDomaineId() != null) {
            Domaine domaine = domaineRepository.findById(request.getDomaineId())
                    .orElseThrow(() -> new EntityNotFoundException("Domaine introuvable"));
            cours.setDomaine(domaine);
        }
        return coursRepository.save(cours);
    }

    public void delete(Long id) {
        Cours cours = getById(id);
        // Un cours avec des sessions ne peut pas être supprimé
        if (sessionRepository.existsByCours_Id(id)) {
            throw new IllegalStateException(
                    "Impossible de supprimer un cours qui a des sessions"
            );
        }
        coursRepository.delete(cours);
    }

    public Cours toggleActif(Long id, boolean actif) {
        Cours cours = getById(id);
        cours.setActif(actif);
        return coursRepository.save(cours);
    }

    // ── Prérequis ─────────────────────────────────────────────────────────────

    public Cours ajouterPrerequis(Long coursId, Long prerequisId) {
        if (coursId.equals(prerequisId)) {
            throw new IllegalArgumentException("Un cours ne peut pas être son propre prérequis");
        }
        Cours cours = getById(coursId);
        Cours prerequis = getById(prerequisId);

        boolean dejaPresent = cours.getPrerequis().stream()
                .anyMatch(p -> p.getId().equals(prerequisId));
        if (dejaPresent) {
            throw new IllegalStateException("Ce prérequis existe déjà");
        }
        // Vérifie qu'on ne crée pas de cycle : prerequis ne doit pas avoir coursId comme prérequis
        if (createsCycle(prerequis, coursId)) {
            throw new IllegalStateException(
                    "Ajout impossible : créerait une dépendance circulaire"
            );
        }
        cours.getPrerequis().add(prerequis);
        return coursRepository.save(cours);
    }

    public Cours retirerPrerequis(Long coursId, Long prerequisId) {
        Cours cours = getById(coursId);
        cours.getPrerequis().removeIf(p -> p.getId().equals(prerequisId));
        return coursRepository.save(cours);
    }

    // ── Documents ─────────────────────────────────────────────────────────────

    public List<Document> getDocuments(Long coursId) {
        getById(coursId); // vérifie que le cours existe
        return documentRepository.findByCours_Id(coursId);
    }

    public Document ajouterDocument(Long coursId, CreateDocumentRequest request) {
        Cours cours = getById(coursId);
        Document document = new Document();
        document.setCours(cours);
        document.setTitre(request.getTitre());
        document.setReference(request.getReference());
        document.setCheminFichier(request.getCheminFichier());
        document.setType(request.getType());
        return documentRepository.save(document);
    }

    public Document updateDocument(Long documentId, CreateDocumentRequest request) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException("Document introuvable"));
        document.setTitre(request.getTitre());
        document.setReference(request.getReference());
        document.setCheminFichier(request.getCheminFichier());
        document.setType(request.getType());
        return documentRepository.save(document);
    }

    public void deleteDocument(Long documentId) {
        documentRepository.deleteById(documentId);
    }

    // ── Privé ─────────────────────────────────────────────────────────────────

    private boolean createsCycle(Cours cours, Long targetId) {
        if (cours.getId().equals(targetId)) return true;
        return cours.getPrerequis().stream()
                .anyMatch(p -> createsCycle(p, targetId));
    }
}
