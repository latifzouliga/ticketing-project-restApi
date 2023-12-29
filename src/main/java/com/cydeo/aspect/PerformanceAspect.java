package com.cydeo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class PerformanceAspect {

    Logger logger = LoggerFactory.getLogger(PerformanceAspect.class);

    @Pointcut("@annotation(com.cydeo.annotation.ExecutionTime)")
    private void anyExecutionTimeOperation() {
    }

    @Around("anyExecutionTimeOperation()")
    public Object anyExecutionTimeOperation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Long beforeTime = System.currentTimeMillis();
        logger.info("Execution will start");
        Object result = proceedingJoinPoint.proceed();
        Long afterTime = System.currentTimeMillis();

        logger.info(
                "Time taken to execute: {} ms - Method: {} - Paremeters: {}",
                (afterTime - beforeTime),
                proceedingJoinPoint.getSignature().toShortString(),
                proceedingJoinPoint.getArgs()
        );
        return result;

    }


}
