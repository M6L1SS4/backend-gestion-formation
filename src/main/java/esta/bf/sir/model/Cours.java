package esta.bf.sir.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import esta.bf.sir.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Cours extends BaseEntity {

    private String titre;
    private String description;
    private Integer duree;
    private String niveau;

    @JsonIgnore
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Session> sessions;

    @JsonIgnore
    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Document> documents;
}
