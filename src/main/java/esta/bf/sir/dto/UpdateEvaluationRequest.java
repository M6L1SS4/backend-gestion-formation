package esta.bf.sir.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEvaluationRequest {
    private String titre;
    private String description;
    private String dateDebut;
    private String dateFin;
    private int dureeMinutes;
    private double noteMaximale;
    private double seuilReussite;
    // domaineId absent — on ne change pas le domaine après création
}
