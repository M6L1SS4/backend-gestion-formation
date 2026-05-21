package esta.bf.sir.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionResponse {
    private Long id;
    private String enonce;
    private String type;
}
