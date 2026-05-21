package esta.bf.sir.dto;

import esta.bf.sir.model.enums.TypeQuestion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuestionSondageRequest {
    private String enonce;
    private TypeQuestion type;
    private Integer ordre;
}
