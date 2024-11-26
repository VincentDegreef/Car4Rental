package be.ucll.se.groep26backend.Auth;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@SuppressWarnings("deprecation")
@Component
public class JwtUtil {
    

    private static final long JWT_EXPIRATION_TIME = 3600000;
    private final SecretKey JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";


    public JwtUtil() {
        this.jwtParser = Jwts.parser().setSigningKey(JWT_SECRET).build();
    }


    public String createToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Add user's email to claims
        claims.put("email", userDetails.getUsername());
        // Add user's roles to claims
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);
        // Generate token
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }  


    private Claims parseJwtClaims(String token) {
        System.out.println("Blabla: " + jwtParser.parseClaimsJws(token).getBody());
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) {
        return claims.getExpiration().after(new Date());
    }
  
    public String getEmail(Claims claims) {
        return claims.getSubject();
    }
}