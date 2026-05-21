package esta.bf.sir.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEvaluationRequest {
    private String titre;
    private String description;
    private Long domaineId;
    private Long sessionId;       // nullable
    private String dateDebut;
    private String dateFin;
    private int dureeMinutes;
    private double noteMaximale;
    private double seuilReussite;
}
