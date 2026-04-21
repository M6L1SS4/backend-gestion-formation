package esta.bf.sir.controller;

import esta.bf.sir.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@PreAuthorize("hasAnyRole('RESPONSABLE_FORMATION', 'ADMIN')")
public class StatsController {

    @Autowired
    private FormateurService formateurService;

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private InscriptionService inscriptionService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private SessionService sessionService;

    @GetMapping("/formateurs/count")
    public ResponseEntity<Integer> getTotalFormateurs() {
        return ResponseEntity.ok(formateurService.getTotalFormateurs());
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
