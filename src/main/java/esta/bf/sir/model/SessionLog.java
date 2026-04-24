package esta.bf.sir.model;

import esta.bf.sir.model.base.BaseEntity;
import esta.bf.sir.model.enums.TypeActionSession;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// model/audit/SessionLog.java
@Entity
@Table(name = "session_logs")
@Data
public class SessionLog extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "session_id", nullable = true)
    private Session session;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = true)
    private Utilisateur utilisateur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeActionSession action;

    private String details;

    @Column(nullable = false)
    private LocalDateTime dateAction;
}
