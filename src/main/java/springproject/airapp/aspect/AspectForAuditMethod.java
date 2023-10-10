package coherentsolutions.airapp.aspect;

import coherentsolutions.airapp.model.entity.MethodInvocations;
import coherentsolutions.airapp.service.MethodInvocationsService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;

import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AspectForAuditMethod {

    private static final Logger LOGGER = LoggerFactory.getLogger(AspectForAuditMethod.class);
    private final MethodInvocationsService methodInvocationsService;

    @Pointcut("@annotation(coherentsolutions.airapp.annotations.Audit)")
    public void onAuditMethods() {
    }

    @Around("onAuditMethods()")
    public void beforeEachMethod(ProceedingJoinPoint proceedingJoinPoint) {
        MethodInvocations methodInvocations = new MethodInvocations();

        Signature signature = proceedingJoinPoint.getSignature();
        methodInvocations.setSignature(signature.toString());
        LOGGER.info(signature.getName());

        Class returnType = ((MethodSignature) signature).getReturnType();
        methodInvocations.setReturnValue(returnType.getSimpleName());

        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) {
            LOGGER.info(arg.toString());
            methodInvocations.setArgument(arg.toString());
        }

        methodInvocationsService.save(methodInvocations);
    }

    @AfterThrowing(pointcut = "onAuditMethods()", throwing = "error")
    public void afterThrowingError(JoinPoint joinPoint, Throwable error) {
        LOGGER.error("Method {} throw exception {}", joinPoint.getSignature(), error);
        MethodInvocations methodInvocations = new MethodInvocations();
        methodInvocations.setError(error.getMessage());
        methodInvocationsService.save(methodInvocations);
    }

}
