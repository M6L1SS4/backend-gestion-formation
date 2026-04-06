package esta.bf.sir.service;

import esta.bf.sir.model.Cours;
import esta.bf.sir.repository.CoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoursService {
    
    @Autowired
    private CoursRepository coursRepository;
    
    public List<Cours> getAllCours() {
        return coursRepository.findAll();
    }
    
    public Optional<Cours> getCoursById(Long id) {
        return coursRepository.findById(id);
    }
    
    public Cours createCours(Cours cours) {
        return coursRepository.save(cours);
    }
    
    public Cours updateCours(Long id, Cours coursDetails) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé"));
        
        cours.setTitre(coursDetails.getTitre());
        cours.setDescription(coursDetails.getDescription());
        cours.setDuree(coursDetails.getDuree());
        cours.setNiveau(coursDetails.getNiveau());
        
        return coursRepository.save(cours);
    }
    
    public void deleteCours(Long id) {
        coursRepository.deleteById(id);
    }
}
