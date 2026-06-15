package br.edu.faculdade.feedback.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import br.edu.faculdade.feedback.model.entity.Usuario;

import java.util.Date;

public class JwtUtil {

    private static final String SECRET_KEY = "segredo";
    private static final String ISSUER = "minha-api";
    private static final long EXPIRATION_TIME = 86400000;

    public static String generateToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim("userId", usuario.getId())
                .withClaim("email", usuario.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    public static DecodedJWT verifyToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build()
                .verify(token);
    }
}
