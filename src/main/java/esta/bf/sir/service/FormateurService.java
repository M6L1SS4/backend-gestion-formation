package esta.bf.sir.service;

import esta.bf.sir.model.Formateur;
import esta.bf.sir.repository.FormateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormateurService {

    @Autowired
    private FormateurRepository formateurRepository;

    public List<Formateur> getAllFormateurs() {
        return formateurRepository.findAll();
    }

    public Formateur createFormateur(Formateur formateur) {
        return formateurRepository.save(formateur);
    }

    public Formateur updateFormateur(Long id, Formateur formateur) {
        return formateurRepository.save(formateur);
    }

    public void deleteFormateur(Long id) {
        formateurRepository.deleteById(id);
    }

    public Formateur getFormateurById(Long id) {
        return formateurRepository.findById(id).orElse(null);
    }

    public int getTotalFormateurs() {
        return (int) formateurRepository.count();
    }
}
