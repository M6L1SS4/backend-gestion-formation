package esta.bf.sir.dto.response;

import esta.bf.sir.model.enums.StatutEvaluation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluationResponse {
    private Long id;
    private String titre;
    private String description;

    private double noteMaximale;
    private double seuilReussite;
    private StatutEvaluation statut;


    private List<QuestionResponse> questions;
}
