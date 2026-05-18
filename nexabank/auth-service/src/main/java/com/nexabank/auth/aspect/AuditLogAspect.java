package com.nexabank.auth.aspect;

import com.nexabank.auth.annotation.AuditLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * AOP Aspect for audit logging.
 * Demonstrates Java 17 AOP + Custom Annotations pattern.
 * Intercepts all methods annotated with @AuditLog.
 */
@Aspect
@Component
@Slf4j
public class AuditLogAspect {

    @Around("@annotation(auditLog)")
    public Object logAuditAction(ProceedingJoinPoint joinPoint, AuditLog auditLog) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("[AUDIT] Action: {} | Method: {} | Description: {}",
                auditLog.action(),
                joinPoint.getSignature().getName(),
                auditLog.description());

        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;
            log.info("[AUDIT] Action: {} completed successfully in {}ms", auditLog.action(), elapsed);
            return result;
        } catch (Throwable ex) {
            log.error("[AUDIT] Action: {} FAILED after {}ms | Error: {}",
                    auditLog.action(),
                    System.currentTimeMillis() - start,
                    ex.getMessage());
            throw ex;
        }
    }
}
