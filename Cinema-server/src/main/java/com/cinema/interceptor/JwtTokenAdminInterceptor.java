package com.cinema.interceptor;

import com.cinema.constant.JwtClaimsConstant;
import com.cinema.context.BaseContext;
import com.cinema.properties.JwtProperties;
import com.cinema.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("当前的id�?+ Thread.currentThread().getId());
        //判断当前拦截到的是Controller的方法还是其他资�?
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());
        
        log.info("请求路径: {}", request.getRequestURI());
        log.info("请求头中的token名称: {}", jwtProperties.getAdminTokenName());
        log.info("请求头中的token�? {}", token);

        //2、校验令�?
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("当前员工id�?, empId);
            BaseContext.setCurrentId(empId);
            //3、通过，放�?
            return true;
        } catch (Exception ex) {
            //4、不通过，响�?01状态码
            log.error("jwt校验失败", ex);
            response.setStatus(401);
            return false;
        }
    }
}
