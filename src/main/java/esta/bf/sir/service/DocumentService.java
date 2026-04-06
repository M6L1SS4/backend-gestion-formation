package esta.bf.sir.service;

import esta.bf.sir.model.Document;
import esta.bf.sir.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {
    
    @Autowired
    private DocumentRepository documentRepository;
    
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }
    
    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }
    
    public Document createDocument(Document document) {
        return documentRepository.save(document);
    }
    
    public Document updateDocument(Long id, Document documentDetails) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document non trouvé"));
        
        document.setTitre(documentDetails.getTitre());
        document.setType(documentDetails.getType());
        document.setUrl(documentDetails.getUrl());
        document.setCours(documentDetails.getCours());
        
        return documentRepository.save(document);
    }
    
    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
}
