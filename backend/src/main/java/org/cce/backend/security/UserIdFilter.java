package org.cce.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.cce.backend.entity.User;
import org.cce.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserIdFilter extends OncePerRequestFilter {
    @Autowired
    UserRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(SecurityContextHolder.getContext().getAuthentication() != null){
            String username = SecurityUtil.getCurrentUsername();
            User user = userRepository.findById(username)
                    .orElseThrow(()->new UsernameNotFoundException("Username "+username+" not found"));
            request.setAttribute("userId",user.getUsername());
            System.out.println("id "+user.getUsername());
        }
        filterChain.doFilter(request,response);
    }
}
