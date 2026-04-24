package esta.bf.sir.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SoumissionRequest {
    private Map<Long, Long> reponsesQcm;      // questionId → choixId
    private Map<Long, String> reponsesLibres; // questionId → texte
}
