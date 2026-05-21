package esta.bf.sir.dto.response;

import esta.bf.sir.model.Inscription;
import esta.bf.sir.model.ResultatEvaluation;
import esta.bf.sir.model.Utilisateur;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CandidatDetailResponse {
    private Utilisateur candidat;
    private List<Inscription> inscriptions;
    private List<ResultatEvaluation> resultats;
}
