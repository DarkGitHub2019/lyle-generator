package com.lyle.product.generator.filter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class UserInfoFilter extends HttpFilter {
    ThreadLocal<Long> threadLocal=new ThreadLocal<>();
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie :cookies) {
            "userId".equals(cookie.getName());
            threadLocal.set(Long.parseLong(cookie.getValue()));
        }
        super.doFilter(request, response, chain);
    }
}
