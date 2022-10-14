package br.uniamerica.pwmanager.utils;

import br.uniamerica.pwmanager.entity.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static br.uniamerica.pwmanager.utils.HttpConstants.*;

@Component
public class JwtUtils {
    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getExpiresAt();
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SIGNING_KEY.getBytes());
    }

    private DecodedJWT getDecodedJWT(String token) {
        JWTVerifier verifier = JWT.require(getAlgorithm()).build();

        return verifier.verify(token);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateAccessToken(User user, String issuer) {
        return doGenerateAccessToken(
                user.getUsername(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()),
                issuer
        );
    }

    public String generateRefreshToken(User user, String issuer) {
        return doGenerateRefreshToekn(
                user.getUsername(),
                issuer
        );
    }

    private String doGenerateAccessToken(String subject, List<String> roles, String issuer) {

        Algorithm algorithm = getAlgorithm();
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .sign(algorithm);
    }

    private String doGenerateRefreshToekn(String subject, String issuer) {
        Algorithm algorithm = getAlgorithm();
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_SECONDS * 1000))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
                username.equals(userDetails.getUsername())
                        && !isTokenExpired(token));
    }
}
