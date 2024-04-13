package org.cce.backend.service;


import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.cce.backend.entity.Token;
import org.cce.backend.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {

    private String secretKey;
    private JwtParser parser;

    @Autowired
    private TokenRepository tokenRepository;

    private final int keyDuration = 24*60*60*1000;
    public JwtService() {
        this.secretKey = "0a287a2e5c073eb8a7e049d914252e8ab1c4620bd8cba1b0ad594c3f7313ea8a";
        parser = Jwts.parser().setSigningKey(getSignInKey()).build();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) throws JwtException {
        return parser.parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(
            UserDetails userDetails
    ){
        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(
            Map<String,Object> extraClaims,
            UserDetails userDetails
    ){
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + keyDuration))
                .setHeaderParam("typ","JWT")
                .signWith(getSignInKey(),SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = extractUsername(token);
        boolean isTokenValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        return isTokenValid && tokenRepository.findByTokenKey(token)
                .stream()
                .findFirst().map(t->t.getIsValid()).orElse(false);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
}
