package esta.bf.sir.service;

import esta.bf.sir.exeption.RefreshTokenException;
import esta.bf.sir.model.RefreshToken;
import esta.bf.sir.model.Utilisateur;
import esta.bf.sir.repository.RefreshTokenRepository;
import esta.bf.sir.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UtilisateurRepository utilisateurRepository;

    private static final long REFRESH_TOKEN_DUREE_JOURS = 7;

    public RefreshToken creerOuRenouveler(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));

        // Un seul refresh token par utilisateur — on remplace l'ancien
        refreshTokenRepository.findByUtilisateur_Id(utilisateurId)
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUtilisateur(utilisateur);
        refreshToken.setExpirationDate(
                LocalDateTime.now().plusDays(REFRESH_TOKEN_DUREE_JOURS)
        );
        refreshToken.setRevoque(false);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken valider(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Refresh token introuvable"));

        if (refreshToken.isExpire()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Refresh token expiré — reconnectez-vous");
        }
        return refreshToken;
    }

    public void revoquer(Long utilisateurId) {
        refreshTokenRepository.findByUtilisateur_Id(utilisateurId)
                .ifPresent(rt -> {
                    rt.setRevoque(true);
                    refreshTokenRepository.save(rt);
                });
    }
}
