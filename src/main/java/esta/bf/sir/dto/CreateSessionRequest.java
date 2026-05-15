package esta.bf.sir.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateSessionRequest {
    private String lieu;
    private Integer capacite;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Long coursId; // On utilise l'ID ici
    private Long formateurInterneId;
    private Long formateurExterneId;
}
