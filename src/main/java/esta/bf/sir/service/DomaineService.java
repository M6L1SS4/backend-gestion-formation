package esta.bf.sir.service;

import esta.bf.sir.model.Domaine;
import esta.bf.sir.repository.CoursRepository;
import esta.bf.sir.repository.DomaineRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DomaineService {

    private final DomaineRepository domaineRepository;
    private final CoursRepository coursRepository;

    public List<Domaine> getAll() {
        return domaineRepository.findAll();
    }

    public Domaine getById(Long id) {
        return domaineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Domaine introuvable : " + id));
    }

    public Domaine create(Domaine domaine) {
        if (domaineRepository.existsByNom(domaine.getNom())) {
            throw new IllegalStateException("Un domaine avec ce nom existe déjà");
        }
        return domaineRepository.save(domaine);
    }

    public Domaine update(Long id, Domaine updated) {
        Domaine domaine = getById(id);
        domaine.setNom(updated.getNom());
        domaine.setDescription(updated.getDescription());
        return domaineRepository.save(domaine);
    }

    public void delete(Long id) {
        // Vérifie qu'aucun cours n'utilise ce domaine
        if (coursRepository.existsByDomaine_Id(id)) {
            throw new IllegalStateException(
                    "Impossible de supprimer un domaine utilisé par des cours"
            );
        }
        domaineRepository.deleteById(id);
    }
}
