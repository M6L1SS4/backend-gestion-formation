package esta.bf.sir.controller.admin;

import esta.bf.sir.dto.CreateCoursRequest;
import esta.bf.sir.dto.CreateDocumentRequest;
import esta.bf.sir.dto.UpdateCoursRequest;
import esta.bf.sir.model.Cours;
import esta.bf.sir.model.Document;
import esta.bf.sir.service.CoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cours")
@PreAuthorize("hasAnyRole('ADMIN', 'RESPONSABLE_FORMATION')")
@RequiredArgsConstructor
public class CoursAdminController {

    private final CoursService coursService;

    @GetMapping
    public ResponseEntity<List<Cours>> getAll() {
        return ResponseEntity.ok(coursService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cours> getById(@PathVariable Long id) {
        return ResponseEntity.ok(coursService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Cours> create(@RequestBody CreateCoursRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(coursService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cours> update(@PathVariable Long id,
                                        @RequestBody UpdateCoursRequest request) {
        return ResponseEntity.ok(coursService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        coursService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activer")
    public ResponseEntity<Cours> activer(@PathVariable Long id) {
        return ResponseEntity.ok(coursService.toggleActif(id, true));
    }

    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<Cours> desactiver(@PathVariable Long id) {
        return ResponseEntity.ok(coursService.toggleActif(id, false));
    }

    @PostMapping("/{id}/prerequis/{prerequisId}")
    public ResponseEntity<Cours> ajouterPrerequis(@PathVariable Long id,
                                                  @PathVariable Long prerequisId) {
        return ResponseEntity.ok(coursService.ajouterPrerequis(id, prerequisId));
    }

    @DeleteMapping("/{id}/prerequis/{prerequisId}")
    public ResponseEntity<Cours> retirerPrerequis(@PathVariable Long id,
                                                  @PathVariable Long prerequisId) {
        return ResponseEntity.ok(coursService.retirerPrerequis(id, prerequisId));
    }

    @GetMapping("/{coursId}/documents")
    public ResponseEntity<List<Document>> getDocuments(@PathVariable Long coursId) {
        return ResponseEntity.ok(coursService.getDocuments(coursId));
    }

    @PostMapping("/{coursId}/documents")
    public ResponseEntity<Document> ajouterDocument(
            @PathVariable Long coursId,
            @RequestBody CreateDocumentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(coursService.ajouterDocument(coursId, request));
    }

    @PutMapping("/{coursId}/documents/{documentId}")
    public ResponseEntity<Document> updateDocument(
            @PathVariable Long documentId,
            @RequestBody CreateDocumentRequest request) {
        return ResponseEntity.ok(coursService.updateDocument(documentId, request));
    }

    @DeleteMapping("/{coursId}/documents/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) {
        coursService.deleteDocument(documentId);
        return ResponseEntity.noContent().build();
    }
}
