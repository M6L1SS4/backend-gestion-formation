package esta.bf.sir.controller.admin;

import esta.bf.sir.model.Domaine;
import esta.bf.sir.service.DomaineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/domaines")
@PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_FORMATION')")
@RequiredArgsConstructor
public class DomaineAdminController {

    private final DomaineService domaineService;

    @GetMapping
    public ResponseEntity<List<Domaine>> getAll() {
        return ResponseEntity.ok(domaineService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Domaine> getById(@PathVariable Long id) {
        return ResponseEntity.ok(domaineService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Domaine> create(@RequestBody Domaine domaine) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(domaineService.create(domaine));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Domaine> update(@PathVariable Long id,
                                          @RequestBody Domaine domaine) {
        return ResponseEntity.ok(domaineService.update(id, domaine));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        domaineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
