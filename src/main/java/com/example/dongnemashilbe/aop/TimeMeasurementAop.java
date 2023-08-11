package com.example.dongnemashilbe.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Aspect
@Component
public class TimeMeasurementAop {
    private static final Logger logger = LoggerFactory.getLogger(TimeMeasurementAop.class);

    // 조인포인트를 어노테이션으로 설정
    @Pointcut("@annotation(com.example.dongnemashilbe.aop.ExeTimer)")
    private void timer(){};

    // 메서드 실행 전,후로 시간을 공유해야 하기 때문
    @Around("timer()")
    public Object AssumeExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();

        try {
            stopWatch.start();
            return joinPoint.proceed();
        }finally {
            stopWatch.stop();

            long totalTimeMillis = stopWatch.getTotalTimeMillis();

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String methodName = signature.getMethod().getName();

            logger.error("실행 메서드: {}, 실행시간 = {}ms", methodName, totalTimeMillis);
        }
         // 조인포인트의 메서드 실행

    }
}
