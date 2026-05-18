package com.nexabank.account.aspect;

import com.nexabank.account.annotation.AuditLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * AOP Aspect for audit logging in account-service.
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
            log.info("[AUDIT] Action: {} completed in {}ms", auditLog.action(), System.currentTimeMillis() - start);
            return result;
        } catch (Throwable ex) {
            log.error("[AUDIT] Action: {} FAILED: {}", auditLog.action(), ex.getMessage());
            throw ex;
        }
    }
}
