package esta.bf.sir.dto;

import esta.bf.sir.model.enums.TypeDocument;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDocumentRequest {
    private String titre;
    private String reference;
    private String cheminFichier;
    private TypeDocument type;
}

