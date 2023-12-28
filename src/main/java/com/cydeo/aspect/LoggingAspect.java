package com.cydeo.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


@Aspect
@Configuration
public class LoggingAspect {

    Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // getting the username of the authorized user
    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount details = (SimpleKeycloakAccount) authentication.getDetails();
        return details.getKeycloakSecurityContext().getToken().getPreferredUsername();
    }

    @Before("execution(* com.cydeo.controller.*.*(..))")
    public void aa() {
        logger.info("\n\n----------------- Logging started ---------------");
    }

    @Pointcut("execution(* com.cydeo.controller.ProjectController.*(..)) || execution(* com.cydeo.controller.TaskController.*(..))")
    private void anyControllerOperation() {
    }

    @Before("anyControllerOperation()")
    public void anyBeforeControllerOperationAdvice(JoinPoint joinPoint) {
        String authorizedUser = getUserName();
        logger.info(
                "Before () -> User: {} - Method: {} - Parameter: {}",
                authorizedUser,
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs()
        );
    }

    @AfterReturning(pointcut = "anyControllerOperation()", returning = "results")
    public void anyAfterReturningControllerOperationAdvice(JoinPoint joinPoint, Object results) {
        String authorizedUser = getUserName();
        logger.info("AfterReturning -> User: {} -> Method: {} -> Results: {}",
                authorizedUser,
                joinPoint.getSignature().toShortString(),
                results.toString());
    }

    @AfterThrowing(pointcut = "anyControllerOperation()", throwing = "exception")
    public void anyAfterThrowingControllerOperationAdvice(JoinPoint joinPoint, Exception exception){
        String authorizedUser = getUserName();
        logger.info("AfterThrowing -> User: {} -> Method: {} -> Exception: {}",
                authorizedUser,
                joinPoint.getSignature().toShortString(),
                exception.getMessage());
    }


}
