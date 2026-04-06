package esta.bf.sir.controller;

import esta.bf.sir.model.Cours;
import esta.bf.sir.model.Inscription;
import esta.bf.sir.model.Session;
import esta.bf.sir.service.CoursService;
import esta.bf.sir.service.InscriptionService;
import esta.bf.sir.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidat")
@PreAuthorize("hasRole('CANDIDAT')")
public class CandidatController {
    
    @Autowired
    private CoursService coursService;
    
    @Autowired
    private SessionService sessionService;
    
    @Autowired
    private InscriptionService inscriptionService;
    
    // Consulter le catalogue des cours
    @GetMapping("/catalogue")
    public ResponseEntity<List<Cours>> getCatalogue() {
        return ResponseEntity.ok(coursService.getAllCours());
    }
    
    // Consulter les sessions disponibles
    @GetMapping("/sessions")
    public ResponseEntity<List<Session>> getSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }
    
    // S'inscrire à une session
    @PostMapping("/sessions/{sessionId}/inscrire")
    public ResponseEntity<Inscription> inscrireSession(@PathVariable Long sessionId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        Session session = sessionService.getSessionById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));
        
        Inscription inscription = new Inscription();
        // Note: Il faudra récupérer le participant connecté depuis l'authentification
        // et l'associer à l'inscription
        
        return ResponseEntity.ok(inscriptionService.createInscription(inscription));
    }
    
    // Consulter mes inscriptions
    @GetMapping("/inscriptions")
    public ResponseEntity<List<Inscription>> getMesInscriptions() {
        // Note: Filtrer par l'utilisateur connecté
        return ResponseEntity.ok(inscriptionService.getAllInscriptions());
    }
}
