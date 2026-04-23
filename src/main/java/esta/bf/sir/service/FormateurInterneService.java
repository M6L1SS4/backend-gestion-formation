package esta.bf.sir.service;

import esta.bf.sir.model.FormateurInterne;
import esta.bf.sir.repository.FormateurInterneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormateurInterneService {

    @Autowired
    private FormateurInterneRepository formateurInterneRepository;

    public List<FormateurInterne> getAllFormateurs() {
        return formateurInterneRepository.findAll();
    }

    public FormateurInterne createFormateur(FormateurInterne formateurInterne) {
        return formateurInterneRepository.save(formateurInterne);
    }

    public FormateurInterne updateFormateur(Long id, FormateurInterne formateurInterne) {
        return formateurInterneRepository.save(formateurInterne);
    }

    public void deleteFormateur(Long id) {
        formateurInterneRepository.deleteById(id);
    }

    public FormateurInterne getFormateurById(Long id) {
        return formateurInterneRepository.findById(id).orElse(null);
    }

    public int getTotalFormateurs() {
        return (int) formateurInterneRepository.count();
    }
}
