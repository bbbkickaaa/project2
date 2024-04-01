package com.API.User.Security;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 또는 다른 적절한 상태 코드
        response.setContentType("application/json;charset=UTF-8");
        String errorMessage;

        if (authException instanceof LockedException) {
            errorMessage = "밴 되었습니다. bbbkickaaa@gmail.com 해당 메일로 문의 바랍니다.";
        } else if (authException instanceof UsernameNotFoundException) {
            errorMessage = "비밀번호가 맞지 않습니다.";
            }
            else if(authException instanceof DisabledException) {
            	errorMessage = "탈퇴된 계정입니다.";
        } else {
            errorMessage = "로그인에 실패했습니다.";
        }
        response.getWriter().write("{ \"error\": \"" + errorMessage + "\" }");
	}
}
