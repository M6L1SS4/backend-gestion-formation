package esta.bf.sir.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReponseSondageRequest {
    private Long questionId;
    private Long optionId;        // null si réponse libre
    private String reponseLibre;  // null si QCM
}
