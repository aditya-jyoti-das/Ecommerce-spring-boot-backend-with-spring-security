package com.aditya.Ecommerce_java_spring_boot_backend.Service;

import ch.qos.logback.core.encoder.EchoEncoder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {



    private static final long EXPIRATION_TIME = 1000*60*2 ;

    private  final String secretGenKey;

    public JwtService() throws NoSuchAlgorithmException {
        this.secretGenKey=GenerateRandomKey();
    }
    public Key getKey(){

        byte[] decode = Decoders.BASE64.decode(this.secretGenKey);
        return Keys.hmacShaKeyFor(decode);
    }


    public String generateToken(String username)  {

        Map<String, Object> claims = new HashMap<>();
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts
                .builder()
                .claims(claims)
                .subject(username)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }


    private String GenerateRandomKey() throws NoSuchAlgorithmException {
        try{
            KeyGenerator instance = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = instance.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }
        catch(NoSuchAlgorithmException e){
            throw new NoSuchAlgorithmException("Algorithm exception");
        }
        catch(Exception e){
            throw new RuntimeException("Runtime exception");
        }
    }


    public <T>  T extractClaims(String token ,Function<Claims,T> claimResolver){
        Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token).getBody();
    }

    public String getUsernameFromToken(String token) {
        return extractClaims(token,Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username=getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpirationDateIsValid(token));
    }
    public Date getExpirationTime(String token){
        return extractClaims(token,Claims::getExpiration);
    }
    public boolean isTokenExpirationDateIsValid(String token){
        return getExpirationTime(token).before(new Date());
    }


}
