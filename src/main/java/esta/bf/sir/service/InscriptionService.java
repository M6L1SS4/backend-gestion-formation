package esta.bf.sir.service;

import esta.bf.sir.model.Inscription;
import esta.bf.sir.repository.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InscriptionService {
    
    @Autowired
    private InscriptionRepository inscriptionRepository;
    
    public List<Inscription> getAllInscriptions() {
        return inscriptionRepository.findAll();
    }
    
    public Optional<Inscription> getInscriptionById(Long id) {
        return inscriptionRepository.findById(id);
    }
    
    public Inscription createInscription(Inscription inscription) {
        return inscriptionRepository.save(inscription);
    }
    
    public Inscription updateInscription(Long id, Inscription inscriptionDetails) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscription non trouvée"));
        
        inscription.setStatut(inscriptionDetails.getStatut());
        inscription.setPresence(inscriptionDetails.getPresence());
        inscription.setParticipant(inscriptionDetails.getParticipant());
        inscription.setSession(inscriptionDetails.getSession());
        
        return inscriptionRepository.save(inscription);
    }
    
    public void deleteInscription(Long id) {
        inscriptionRepository.deleteById(id);
    }
}
