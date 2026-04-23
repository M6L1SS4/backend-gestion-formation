package esta.bf.sir.service;

import esta.bf.sir.model.FormateurExterne;
import esta.bf.sir.repository.FormateurExterneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormateurExterneService {

    @Autowired
    FormateurExterneRepository formateurExterneRepository;

    public List<FormateurExterne> getAllFormateursExterne() {
        return formateurExterneRepository.findAll();
    }
}
