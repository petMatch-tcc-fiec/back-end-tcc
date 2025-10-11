package com.PetMatch.PetMatchBackEnd.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Aspecto para logar todas as requisições nos controllers.
 * Registra método, argumentos e tempo de execução.
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    /**
     * Pointcut para todos os métodos em controllers
     */
    @Pointcut("execution(* com.PetMatch.PetMatchBackEnd.features..controllers.*.*(..))")
    public void controllerMethods() {}

    /**
     * Advice que executa antes do método do controller
     */
    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        HttpServletRequest request = getRequest();
        String method = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        log.info("=== REQUISIÇÃO INICIADA ===");
        log.info("Controller: {}", className);
        log.info("Método: {}", method);
        log.info("HTTP Method: {}", request != null ? request.getMethod() : "N/A");
        log.info("URL: {}", request != null ? request.getRequestURI() : "N/A");
        log.info("Argumentos: {}", formatArgs(args));
    }

    /**
     * Advice que executa depois do método do controller (sucesso)
     */
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String method = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("=== REQUISIÇÃO FINALIZADA COM SUCESSO ===");
        log.info("Controller: {}", className);
        log.info("Método: {}", method);
        log.info("Response: {}", formatResponse(result));
    }

    /**
     * Advice que executa quando há exceção no controller
     */
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        String method = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.error("=== ERRO NA REQUISIÇÃO ===");
        log.error("Controller: {}", className);
        log.error("Método: {}", method);
        log.error("Exceção: {}", exception.getClass().getSimpleName());
        log.error("Mensagem: {}", exception.getMessage());
    }

    /**
     * Advice ao redor (Around) - captura tempo de execução
     */
    @Around("controllerMethods()")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            Object result = proceedingJoinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.debug("Tempo de execução: {}ms", duration);

            return result;
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.error("Tempo de execução até erro: {}ms", duration);
            throw throwable;
        }
    }

    /**
     * Obtém a requisição HTTP atual
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * Formata os argumentos para log
     */
    private String formatArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return "Sem argumentos";
        }
        return Arrays.toString(args);
    }

    /**
     * Formata a resposta para log
     */
    private String formatResponse(Object result) {
        if (result == null) {
            return "null";
        }
        return result.toString();
    }
}