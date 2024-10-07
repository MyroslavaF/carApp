package com.mfarion.carregistry.filter;

import com.mfarion.carregistry.services.impl.JwtService;
import com.mfarion.carregistry.services.impl.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Obtiene el encabezado de autorización de la solicitud HTTP
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        // Si el encabezado de autorización está vacío o no comienza con "Bearer ", pasa al siguiente filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Authorization header is missing or does not start with Bearer");
            filterChain.doFilter(request, response);
            return;
        }


        //Extrae el token JWT del encabezado de autorización
        jwt = authHeader.substring(7);// Bearer XXXX
        log.info("JWT -> {}", jwt);

        //Extrae el nombre de usuario (email) del token JWT
        userEmail = jwtService.extractUserName(jwt);

        //Si el email no está vacío y no hay una autenticación previa en el contexto de seguridad
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("Extracted userEmail: {}", userEmail);
            // Carga los detalles del usuario utilizando el servicio de usuarios
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);
            // Verifica si el token es válido
            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.debug("User - {}", userDetails);

                // Crea un token de autenticación con los detalles del usuario
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Establece los detalles de autenticación en el token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Asigna el contexto de seguridad al SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }

        }
        filterChain.doFilter(request, response);

    }

}