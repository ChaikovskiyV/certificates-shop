package com.epam.esm.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author VChaikovski
 * @project certificates-shop-backend
 * The type Logging aspect.
 * <p>
 * This class executes logging.
 */
@Aspect
public class LoggingAspect {
    private static final Logger logger = LogManager.getLogger();
    private static final String AFTER_CALLING = "Calling of method: ";
    private static final String AFTER_THROWING = "Exception was thrown in method: ";

    /**
     * Performance.
     */
    @Pointcut("execution(* com.epam.esm.service.*.*(..))")
    public void performance() {
        //Do nothing.
    }

    /**
     * Log before service method.
     *
     * @param joinPoint the join point
     */
    @Before("performance()")
    public void logBeforeServiceMethod(JoinPoint joinPoint) {
        logger.info(() -> AFTER_CALLING + joinPoint.getSignature());
    }

    @AfterThrowing("performance()")
    public void logAfterThrowing(JoinPoint joinPoint) {
        logger.error(() -> AFTER_THROWING + joinPoint.getSignature());
    }
}