package br.uniamerica.pwmanager.filter;

import br.uniamerica.pwmanager.entity.User;
import br.uniamerica.pwmanager.utils.JwtUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = new JwtUtils();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (!HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        try {
            JsonAuthenticationParser auth = new ObjectMapper().readValue(
                    request.getInputStream(), JsonAuthenticationParser.class
            );

            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                    auth.username, auth.password
            );

            return this.authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            response.setContentType(APPLICATION_JSON_VALUE);
            throw new AuthenticationServiceException("Could not parse authentication payload");
        }
    }

    record JsonAuthenticationParser(String username, String password) {
        @JsonCreator
        JsonAuthenticationParser(@JsonProperty("username") String username, @JsonProperty("password") String password) {
            this.username = username;
            this.password = password;
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authentication
    ) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String requestURI = request.getRequestURL().toString();

        String accessToken = jwtUtils.generateAccessToken(user, requestURI);

        String refreshToken = jwtUtils.generateRefreshToken(user, requestURI);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);

        sendJsonResponse(response, tokens, HttpServletResponse.SC_OK);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed
    ) throws IOException, ServletException {
        Map<String, String> error = new HashMap<>();
        error.put("message", failed.getMessage());

        sendJsonResponse(response, error, HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void sendJsonResponse(
            HttpServletResponse response,
            Map<String, String> responseMessage,
            int responseStatus
    ) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(responseStatus);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseMessage));
    }
}
