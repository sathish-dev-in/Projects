package com.autonest.aspect;

import com.autonest.annotation.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * AOP Aspect that intercepts methods annotated with @Loggable.
 * Logs method name, arguments, execution time, and result.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("@annotation(com.autonest.annotation.Loggable) || @within(com.autonest.annotation.Loggable)")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Loggable loggable = method.getAnnotation(Loggable.class);
        if (loggable == null) {
            loggable = joinPoint.getTarget().getClass().getAnnotation(Loggable.class);
        }

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        String label = (loggable != null && !loggable.value().isEmpty()) ? loggable.value() : methodName;

        boolean logArgs = loggable == null || loggable.logArgs();
        boolean logResult = loggable != null && loggable.logResult();

        if (logArgs && joinPoint.getArgs().length > 0) {
            log.info("[AutoNest] >> Entering {}.{}() | args: {}", className, label, Arrays.toString(joinPoint.getArgs()));
        } else {
            log.info("[AutoNest] >> Entering {}.{}()", className, label);
        }

        long startTime = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();
            long elapsedMs = System.currentTimeMillis() - startTime;

            if (logResult && result != null) {
                log.info("[AutoNest] << Exiting {}.{}() | duration: {}ms | result: {}", className, label, elapsedMs, result);
            } else {
                log.info("[AutoNest] << Exiting {}.{}() | duration: {}ms", className, label, elapsedMs);
            }

            return result;
        } catch (Exception ex) {
            long elapsedMs = System.currentTimeMillis() - startTime;
            log.error("[AutoNest] !! Exception in {}.{}() after {}ms | error: {}", className, label, elapsedMs, ex.getMessage());
            throw ex;
        }
    }
}
