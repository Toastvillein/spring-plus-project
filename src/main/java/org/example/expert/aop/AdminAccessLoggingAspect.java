package org.example.expert.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.expert.aop.logdb.Log;
import org.example.expert.aop.logdb.LogRepository;
import org.example.expert.config.jwt.UserAuth;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminAccessLoggingAspect {

    private final HttpServletRequest request;
    private final LogRepository logRepository;

    /* changeUserRole()의 실행전에 로그가 출력되야 하니
    * @After 어노테이션을 @Before 로 변경
    * 또한 잘못된 디렉토리 주소를 변경
    * */
    @Before("execution(* org.example.expert.domain.user.controller.UserAdminController.changeUserRole())")
    public void logAfterChangeUserRole(JoinPoint joinPoint) {
        String userId = String.valueOf(request.getAttribute("userId"));
        String requestUrl = request.getRequestURI();
        LocalDateTime requestTime = LocalDateTime.now();

        log.info("Admin Access Log - User ID: {}, Request Time: {}, Request URL: {}, Method: {}",
                userId, requestTime, requestUrl, joinPoint.getSignature().getName());
    }

    /* 레벨 11 AOP 작성
    * 본래 AuthUserArgumentResolver로 되어있는 컨트롤러이지만
    * 레벨 9에서 Spring Security를 적용하면서 바꿨기 때문에 saveManager 메서드도이에 맞춰서 변경
    * 다른 AuthUser를 사용하는 컨트롤러도 다 변경해줘야 하지만 과제 조건에 해당되는 부분만 변경함
    * */
    @Before("execution(* org.example.expert.domain.manager.controller.ManagerController.saveManager())")
    public void logBeforeSaveManager(JoinPoint joinPoint) throws Throwable{
        // SecurityContext에서 유저 정보 추출
        UserAuth userAuth = (UserAuth) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();

        Long userId = userAuth.getId();
        LocalDateTime requestTime = LocalDateTime.now();
        String requestUrl = request.getRequestURI();

        log.info("Saving Manager Log - User ID: {}, Request Time: {}, Request URL: {}, Method: {}",
            userId,requestTime,requestUrl,joinPoint.getSignature().getName());

        String logMessage = String.format(
            "Saving Manager Log - User ID: %s, Request Time: %s, Request URL: %s, Method: %s",
        userId,requestTime,requestUrl,joinPoint.getSignature().getName()
        );

        // DB에 로그 저장 및 try-catch로 감싸서 예외 무시 처리
		try {
			logRepository.save(new Log(logMessage));
		} catch (Exception e) {
			log.warn("로깅 실패: {}", e.getMessage());
		}
	}
}
