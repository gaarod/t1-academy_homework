package org.t1academy.tasktracker.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Before("@annotation(org.t1academy.tasktracker.aspect.annotation.LogExecution)")
    public void loggingBefore(JoinPoint joinPoint) {
        log.info("Executing method: {} with parameter {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs())
        );
    }

    @AfterReturning(
            pointcut = "@annotation(org.t1academy.tasktracker.aspect.annotation.HandlingResult)",
            returning = "result"
    )
    public void loggingAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Method {} returned result: {}",
                joinPoint.getSignature().getName(),
                result
        );
    }

    @AfterThrowing(
            pointcut = "@annotation(org.t1academy.tasktracker.aspect.annotation.LogException)",
            throwing = "exception"
    )
    public void loggingAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception in method {} with parameter {}",
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs())
        );
        log.error("Exception is {} with message: {}",
                exception.getClass().getName(),
                exception.getMessage(),
                exception
        );
    }

    @Around("@annotation(org.t1academy.tasktracker.aspect.annotation.LogTracking)")
    public Object loggingAround(ProceedingJoinPoint joinPoint) throws Throwable {

        Object proceeded;
        long executionTime;

        try {
            long startTime = System.currentTimeMillis();
            proceeded = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            executionTime = endTime - startTime;
        } catch ( Throwable throwable ) {
            log.error("Aspect failed", throwable);
            throw new RuntimeException("Aspect failed");
        }

        log.info("Execution time {}", executionTime);

        return proceeded;
    }

}
