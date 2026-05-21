package esta.bf.sir.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultatEvaluationResponse {
    private Long id;
    private double noteObtenue;
    private boolean reussi;

    private Long evaluationId;
}
