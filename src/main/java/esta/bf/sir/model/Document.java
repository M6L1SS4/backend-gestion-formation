package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Document extends BaseEntity {

    private String titre;
    private String type;
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cours_id")
    private Cours cours;
}
