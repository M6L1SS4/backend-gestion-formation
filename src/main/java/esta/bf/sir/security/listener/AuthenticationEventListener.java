package esta.bf.sir.security.listener;

import esta.bf.sir.model.audit.SecurityLog;
import esta.bf.sir.service.SecurityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

// security/listener/AuthenticationEventListener.java
@Component
@RequiredArgsConstructor
public class AuthenticationEventListener {

    private final SecurityLogService securityLogService;

    // Login réussi
    @EventListener
    public void onLoginSucces(AuthenticationSuccessEvent event) {
        securityLogService.log(
                SecurityLog.TypeEvenement.LOGIN_SUCCES,
                event.getAuthentication().getName(),
                null
        );
    }

    // Login échoué — mauvais mot de passe, utilisateur inexistant...
    @EventListener
    public void onLoginEchec(AbstractAuthenticationFailureEvent event) {
        securityLogService.log(
                SecurityLog.TypeEvenement.LOGIN_ECHEC,
                event.getAuthentication().getName(),
                event.getException().getMessage()
        );
    }

    // Accès refusé — bon token mais mauvais rôle
    @EventListener
    public void onAccesRefuse(AuthorizationDeniedEvent event) {
        securityLogService.log(
                SecurityLog.TypeEvenement.ACCES_REFUSE,
                // AuthorizationDeniedEvent donne le principal courant
                event.getAuthentication().get().getName(),
                event.getAuthorizationDecision().toString()
        );
    }
}
