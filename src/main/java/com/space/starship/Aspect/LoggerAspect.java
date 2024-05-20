package com.space.starship.Aspect;

import com.space.starship.Controller.StarshipController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Aspect
@Component
public class LoggerAspect {
    private static final Logger logger = LogManager.getLogger(StarshipController.class);

    /** Desarrollar un @Aspect que añada una línea de log cuando nos piden una nave con un id negativo.
    *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution( public * com.space.starship.Controller.*.*(..))")
    public Object logAroundControllers(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        logger.info(MessageFormat.format("Before executing: {0}", methodName));
        logger.info(MessageFormat.format("Arguments: {0}", args.toString()));

        // Si el Id es negativo indicarlo con log.error
        if(methodName.equals("getStarshipsById") && Integer.getInteger(args[1].toString()) < 0){
            logger.error("The Id requested cannot be less than 1!!");
        }

        // And the result
        Object result = joinPoint.proceed();
        logger.info(MessageFormat.format("After executing: {0}", methodName));
        logger.info(MessageFormat.format("Result: {0}", result));

        return result;
    }

}
