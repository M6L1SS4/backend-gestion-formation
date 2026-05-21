package esta.bf.sir.model.map;

import esta.bf.sir.dto.response.EvaluationResponse;
import esta.bf.sir.dto.response.QuestionResponse;
import esta.bf.sir.model.Evaluation;
import esta.bf.sir.model.Question;
import org.springframework.stereotype.Component;

@Component
public class EvaluationMapper {

    public EvaluationResponse toResponse(Evaluation e) {
        EvaluationResponse res = new EvaluationResponse();

        res.setId(e.getId());
        res.setTitre(e.getTitre());
        res.setDescription(e.getDescription());
        res.setNoteMaximale(e.getNoteMaximale());
        res.setSeuilReussite(e.getSeuilReussite());

        if (e.getQuestions() != null) {
            res.setQuestions(
                    e.getQuestions().stream()
                            .map(this::mapQuestion)
                            .toList()
            );
        }

        return res;
    }

    private QuestionResponse mapQuestion(Question q) {
        QuestionResponse r = new QuestionResponse();
        r.setId(q.getId());
        r.setEnonce(q.getEnonce());
        r.setType(q.getType().name());
        return r;
    }
}
