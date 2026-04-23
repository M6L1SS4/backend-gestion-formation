package esta.bf.sir.controller;

import esta.bf.sir.model.*;
import esta.bf.sir.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private CoursService coursService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private FormateurInterneService formateurInterneService;

    // Gestion des utilisateurs
    @GetMapping("/utilisateurs")
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        // Note: Cette méthode nécessite une implémentation personnalisée dans le repository
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/utilisateurs")
    public ResponseEntity<Utilisateur> createUtilisateur(@RequestBody Utilisateur utilisateur) {
        Utilisateur newUtilisateur = utilisateurService.createUtilisateur(utilisateur);
        return ResponseEntity.ok(newUtilisateur);
    }

    // Gestion des cours
    @GetMapping("/cours")
    public ResponseEntity<List<Cours>> getAllCours() {
        return ResponseEntity.ok(coursService.getAllCours());
    }

    @PostMapping("/cours")
    public ResponseEntity<Cours> createCours(@RequestBody Cours cours) {
        return ResponseEntity.ok(coursService.createCours(cours));
    }

//    @PutMapping("/cours/{id}")
//    public ResponseEntity<Cours> updateCours(@PathVariable Long id, @RequestBody Cours cours) {
//        return ResponseEntity.ok(coursService.updateCours(id, cours));
//    }

    @DeleteMapping("/cours/{id}")
    public ResponseEntity<Void> deleteCours(@PathVariable Long id) {
        coursService.deleteCours(id);
        return ResponseEntity.ok().build();
    }

    // Gestion des sessions
    @GetMapping("/sessions")
    public ResponseEntity<List<Session>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @PostMapping("/sessions")
    public ResponseEntity<Session> createSession(@RequestBody Session session) {
        return ResponseEntity.ok(sessionService.createSession(session));
    }

    @PutMapping("/sessions/{id}")
    public ResponseEntity<Session> updateSession(@PathVariable Long id, @RequestBody Session session) {
        return ResponseEntity.ok(sessionService.updateSession(id, session));
    }

    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.ok().build();
    }

    // Gestion des évaluations
    @GetMapping("/evaluations")
    public ResponseEntity<List<Evaluation>> getAllEvaluations() {
        return ResponseEntity.ok(evaluationService.getAllEvaluations());
    }

    @PostMapping("/evaluations")
    public ResponseEntity<Evaluation> createEvaluation(@RequestBody Evaluation evaluation) {
        return ResponseEntity.ok(evaluationService.createEvaluation(evaluation));
    }
//
//    @PutMapping("/evaluations/{id}")
//    public ResponseEntity<Evaluation> updateEvaluation(@PathVariable Long id, @RequestBody Evaluation evaluation) {
//        return ResponseEntity.ok(evaluationService.updateEvaluation(id, evaluation));
//    }

    @DeleteMapping("/evaluations/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
        return ResponseEntity.ok().build();
    }

    // Gestion des documents
    @GetMapping("/documents")
    public ResponseEntity<List<Document>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @PostMapping("/documents")
    public ResponseEntity<Document> createDocument(@RequestBody Document document) {
        return ResponseEntity.ok(documentService.createDocument(document));
    }

//    @PutMapping("/documents/{id}")
//    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody Document document) {
//        return ResponseEntity.ok(documentService.updateDocument(id, document));
//    }

    @DeleteMapping("/documents/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok().build();
    }

    // Gestion des formateurs
    @GetMapping("/formateurs")
    public ResponseEntity<List<FormateurInterne>> getAllFormateurs() {
        return ResponseEntity.ok(formateurInterneService.getAllFormateurs());
    }

    @PostMapping("/formateurs/{id}")
    public ResponseEntity<FormateurInterne> createFormateur(@RequestBody FormateurInterne formateurInterne) {
        return ResponseEntity.ok(formateurInterneService.createFormateur(formateurInterne));
    }

    @PutMapping("/formateurs/{id}")
    public ResponseEntity<FormateurInterne> updateFormateur(@PathVariable Long id, @RequestBody FormateurInterne formateurInterne) {
        return ResponseEntity.ok(formateurInterneService.updateFormateur(id, formateurInterne));
    }

    @DeleteMapping("/formateurs/{id}")
    public ResponseEntity<Void> deleteFormateur(@PathVariable Long id) {
        formateurInterneService.deleteFormateur(id);
        return ResponseEntity.ok().build();
    }

}
