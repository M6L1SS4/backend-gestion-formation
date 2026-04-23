package esta.bf.sir.controller;

import esta.bf.sir.model.Inscription;
import esta.bf.sir.model.Session;
import esta.bf.sir.service.InscriptionService;
import esta.bf.sir.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/formateur")
@PreAuthorize("hasRole('FORMATEUR')")
public class FormateurController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private InscriptionService inscriptionService;

    // Consulter les sessions du formateur
    @GetMapping("/sessions")
    public ResponseEntity<List<Session>> getMesSessions() {
        // Note: Filtrer par le formateur connecté
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    // Consulter les participants d'une session
    @GetMapping("/sessions/{sessionId}/participants")
    public ResponseEntity<List<Inscription>> getParticipantsSession(@PathVariable Long sessionId) {
        // Note: Filtrer les inscriptions par session et par formateur connecté
        return ResponseEntity.ok(inscriptionService.getAllInscriptions());
    }


}
