package com.autonest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom annotation to enable AOP-based method logging.
 * Applied to controller methods to log execution time and method details.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {

    String value() default "";

    boolean logArgs() default true;

    boolean logResult() default false;
}
