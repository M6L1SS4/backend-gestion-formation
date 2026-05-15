package esta.bf.sir.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UpdateSessionRequest {
    private String lieu;
    private Integer capacite;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Long formateurInterneId;
    private Long formateurExterneId;
}
