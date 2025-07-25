package dev.srivatsan.kubeapi.aop;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
@ConditionalOnProperty(prefix = "global", value = "debugModeEnabled", havingValue = "true", matchIfMissing = false)
public class AspectService {

    private static final String POINTCUT_EXEC_FOR_ALL_SERVICE_METHODS = "execution(* dev.srivatsan.kubeapi.service.*.*(..))";

    @Before(POINTCUT_EXEC_FOR_ALL_SERVICE_METHODS)
    public void logBefore(JoinPoint joinPoint) {
        logMethodDetails(true, joinPoint, null);
    }

    @AfterReturning(pointcut = POINTCUT_EXEC_FOR_ALL_SERVICE_METHODS, returning = "value")
    public void logReturn(JoinPoint joinPoint, Object value) {
        logMethodDetails(false, joinPoint, value);
    }

    @AfterThrowing(pointcut = POINTCUT_EXEC_FOR_ALL_SERVICE_METHODS, throwing = "ex")
    public void logException(Exception ex) {
        log.error("Exception message: {}", ex.getMessage());
    }

    private void logMethodDetails(boolean isBefore, JoinPoint joinPoint, Object returnValue) {
        StringBuilder message = new StringBuilder();
        message.append(String.format("Method: '%s'", joinPoint.getSignature().getName()));

        if (isBefore) message.append(" Called :: Params: ").append(Arrays.toString(joinPoint.getArgs()));
        else message.append(" Executed :: Returned value: ").append(returnValue);

        log.info(message.toString());
    }

    @PostConstruct
    public void init() {
        log.info("Aspect Service Initialized");
    }

}