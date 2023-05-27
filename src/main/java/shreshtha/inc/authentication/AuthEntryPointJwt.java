package shreshtha.inc.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized error: {}", authException.getMessage());
        final Map<String, Object> body = new HashMap<>();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if(response.getStatus() == 401) {
        	response.setStatus(HttpStatus.UNAUTHORIZED.value());
        	body.put("error", "Unauthorized");
        }
        if(response.getStatus() == 400) {
        	response.setStatus(HttpStatus.BAD_REQUEST.value());
        	body.put("error", "Bad Request");
        }
        if(response.getStatus() == 404) {
        	response.setStatus(HttpStatus.NOT_FOUND.value());
        	body.put("error", "Not Found");
        }
        if(response.getStatus() == 500) {
        	response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        	body.put("error", "Internal Server Error");
        }
        
        body.put("status", response.getStatus());
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
