package esta.bf.sir.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCoursRequest {
    private String titre;
    private String description;
    private int dureeHeures;
    private Long domaineId; // nullable — pas changé si null
}
