package com.company.tasks.online.store.ws.aop.aspects;


import com.company.tasks.online.store.enums.LoggerParameter;
import com.company.tasks.online.store.utils.JsonHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.simple.JSONObject;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.UUID;
import java.util.stream.IntStream;


@Slf4j
@Aspect
@EnableAspectJAutoProxy
@Configuration
@RequiredArgsConstructor
public class WsMethodLoggerAspect {

    private final JsonHelper jsonHelper;

    @Around("com.company.tasks.online.store.ws.aop.pointcuts.WsPointcut.anyWsMethodCall()")
    public Object logAroundWsCall(final ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            MDC.put(LoggerParameter.UNIQUE_ID.getName(), UUID.randomUUID().toString());
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String methodName = signature.getName();

            log.trace("Started ws method={} with params: {}", methodName, getMethodParameters(joinPoint));
            final Object retVal = joinPoint.proceed();
            log.trace("Ended ws method={}", methodName);

            return retVal;
        } finally {
            MDC.clear();
        }
    }

    private String getMethodParameters(ProceedingJoinPoint joinPoint) {
        String[] parameterNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        Object[] args = joinPoint.getArgs();

        JSONObject jsonObj = new JSONObject();
        IntStream.range(0, args.length).forEach(i -> {
            try {
                jsonObj.put(parameterNames[i], this.jsonHelper.getJsonFromPojo(args[i]));
            } catch (JsonProcessingException e) {
                log.warn("Cant' generate json for request arg={}", args[i]);
            }
        });
        return jsonObj.toString();
    }
}
