package com.lingfei.aspect;

import com.alibaba.fastjson.JSON;
import com.lingfei.annotation.Systemlog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lingfei Wang
 * @version 1.0
 * @date 2022/12/6 13:58
 * @Decription:
 */
@Component
@Aspect
@Slf4j
public class LogAspect{

    @Pointcut("@annotation(com.lingfei.annotation.Systemlog)")
    public void pt(){

    }

    @Around("pt()")
    public Object printlog(ProceedingJoinPoint joinPoint) throws Throwable{

        Object ret;
        try {
            handleBefore(joinPoint);
            ret = joinPoint.proceed();
            handleAfter(ret);
        } finally {
            log.info("=======End=======" + System.lineSeparator());
        }

        return ret;
    }

    private void handleAfter(Object ret) {
        log.info("Response       : {}",JSON.toJSONString(ret) );
    }

    private void handleBefore(ProceedingJoinPoint joinPoint) {
        //RequestAttributes是一个接口，我们需要的方法不在其中，而在其实现类里存在方法
        //于是我们进行强转
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //获取被增强方法上的注解对象
        Systemlog systemlog = getSystemLog(joinPoint);
        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL            : {}",request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName   : {}", systemlog.businessName());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method   : {}.{}", joinPoint.getSignature().getDeclaringTypeName(),((MethodSignature) joinPoint.getSignature()).getName());
        // 打印请求的 IP
        log.info("IP             : {}",request.getRemoteHost());
        // 打印请求入参
        log.info("Request Args   : {}", JSON.toJSONString(joinPoint.getArgs()));
        // 打印出参
        log.info("Response       : {}","" );
        // 结束后换行
        
    }

    private Systemlog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Systemlog systemlog = methodSignature.getMethod().getAnnotation(Systemlog.class);
        return systemlog;
    }

}
