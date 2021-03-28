package kz.nuris.cardgame.configs;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggingConfig {

    @Before("within(kz.nuris.cardgame.chat.ChatSendingOperations*) " +
            "|| within(kz.nuris.cardgame.service.session.*)" +
            "|| within(kz.nuris.cardgame.service.billing.*)" +
            "|| within(kz.nuris.cardgame.service.player.PlayerService)")
    public void logBeforeCall(JoinPoint joinPoint) {
        var strBuilder = new StringBuilder();
        strBuilder.append("start-method: ")
                .append(joinPoint.getSignature());

        if (joinPoint.getArgs().length > 0) {
            strBuilder.append(" with arguments: ")
                    .append(Arrays.toString(joinPoint.getArgs()));
        }

        log.info(strBuilder.toString());
    }

    @AfterReturning(value = "within(kz.nuris.cardgame.service.session.*) " +
            "|| within(kz.nuris.cardgame.service.billing.*)" +
            "|| within(kz.nuris.cardgame.service.player.PlayerService)", returning = "result")
    public void logAfterCall(JoinPoint joinPoint, Object result) {
        var strBuilder = new StringBuilder()
                .append("end-method: ")
                .append(joinPoint.getSignature());

        if (joinPoint.getArgs().length > 0) {
            strBuilder.append(" with arguments: ")
                    .append(Arrays.toString(joinPoint.getArgs()));
        }

        if (result != null) {
            strBuilder.append("\nmethod-result: ")
                    .append(result);
        }

        log.info(strBuilder.toString());
    }

}
