package com.company.tasks.online.store.ws.aop.pointcuts;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class WsPointcut {
    @Pointcut("within(com.company.tasks.online.store.ws.controllers.*)")
    public static void anyWsMethodCall() {
        // Do nothing. This is just a pointcut.
    }
}
