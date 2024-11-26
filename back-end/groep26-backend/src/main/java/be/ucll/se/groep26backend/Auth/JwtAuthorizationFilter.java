package be.ucll.se.groep26backend.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;

import be.ucll.se.groep26backend.user.model.User;
import be.ucll.se.groep26backend.user.service.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter{


    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final ObjectMapper mapper;

    @Autowired
    public JwtAuthorizationFilter(JwtUtil jwtUtil, ObjectMapper mapper, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
        this.userService = userService;
    }

    
    @SuppressWarnings("unchecked")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, Object> errorDetails = new HashMap<>();
        try {
            String accessToken = jwtUtil.resolveToken(request);
            if (accessToken == null ) {
                filterChain.doFilter(request, response);
                return;
            }
            System.out.println("token : "+accessToken);
            Claims claims = jwtUtil.resolveClaims(request);

            List<String> authorities = new ArrayList<String>();
            authorities = claims.get("authorities", List.class);


            if(claims != null & jwtUtil.validateClaims(claims)){
                String email = claims.getSubject();
                User user = userService.getUserByUsername(email);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(email,"",user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }catch (Exception e){
            errorDetails.put("message", "Authentication Error");
            errorDetails.put("details",e.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writeValue(response.getWriter(), errorDetails);

        }
        filterChain.doFilter(request, response);
    }
}