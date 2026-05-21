package esta.bf.sir.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSondageRequest {
    private String titre;
    private String description;
    private Long domaineId;
    private String dateDebut;
    private String dateFin;
    private boolean anonyme;
}
