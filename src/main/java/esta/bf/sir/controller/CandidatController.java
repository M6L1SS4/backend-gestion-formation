package esta.bf.sir.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidat")
@PreAuthorize("hasRole('CANDIDAT')")
public class CandidatController {

}
