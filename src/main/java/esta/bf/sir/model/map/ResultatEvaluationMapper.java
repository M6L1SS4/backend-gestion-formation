package esta.bf.sir.model.map;

import esta.bf.sir.dto.response.ResultatEvaluationResponse;
import esta.bf.sir.model.ResultatEvaluation;
import org.springframework.stereotype.Component;

@Component
public class ResultatEvaluationMapper {

    public ResultatEvaluationResponse toResponse(ResultatEvaluation r) {
        ResultatEvaluationResponse res = new ResultatEvaluationResponse();

        res.setId(r.getId());
        res.setNoteObtenue(r.getNoteObtenue());
        res.setReussi(r.isReussi());

        if (r.getEvaluation() != null) {
            res.setEvaluationId(r.getEvaluation().getId());
        }

        return res;
    }
}
