package br.edu.faculdade.feedback.filter;

import br.edu.faculdade.feedback.util.JwtUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/api/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // Permite acesso público à rota de login
        if (path.endsWith("/api/login")) {
            chain.doFilter(request, response);
            return;
        }

        // Lê o header de Authorization
        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"error\": \"Token de acesso ausente ou mal formatado. Envie no header Authorization: Bearer <token>\"}");
            return;
        }

        // Extrai o token após o prefixo "Bearer "
        String token = authHeader.substring(7);

        try {
            // Verifica a validade e a assinatura do token
            DecodedJWT jwt = JwtUtil.verifyToken(token);
            
            // Extrai a claim que precisamos
            Integer userId = jwt.getClaim("userId").asInt();
            
            // Atribui ao request para que os Controllers possam acessar
            req.setAttribute("usuarioId", userId);
            
            // Prossegue com a requisição para o Controller
            chain.doFilter(request, response);
            
        } catch (JWTVerificationException e) {
            // Token inválido, expirado ou forjado
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write("{\"error\": \"Token inválido ou expirado\"}");
        }
    }

    @Override
    public void destroy() {
    }
}
