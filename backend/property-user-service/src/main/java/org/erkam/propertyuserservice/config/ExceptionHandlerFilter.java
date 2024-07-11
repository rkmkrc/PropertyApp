package org.erkam.propertyuserservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.erkam.propertyuserservice.constants.LogMessage;
import org.erkam.propertyuserservice.constants.enums.MessageStatus;
import org.erkam.propertyuserservice.error.ErrorDetails;
import org.erkam.propertyuserservice.exception.jwt.JwtException;
import org.erkam.propertyuserservice.exception.jwt.JwtExceptionMessage;
import org.erkam.propertyuserservice.exception.user.UserExceptionMessage;
import org.erkam.propertyuserservice.exception.user.UserInfoMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
    This class is implemented to catch the exceptions before the jwt authentication filter
    because I want to show meaningful and informative messages to client
*/
@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            log.error(LogMessage.generate(MessageStatus.NEG, JwtExceptionMessage.EXCEPTION_CAUGHT_IN_FILTER, e.getMessage()));

            // Custom error response
            // NOTE: Do not give any specific exception message due to security reasons
            //  just send "You must login first."

            ErrorDetails errorDetails = new ErrorDetails(HttpStatus.UNAUTHORIZED.value(),
                    UserInfoMessage.YOU_MUST_LOGIN_FIRST,
                    UserExceptionMessage.USER_IS_NOT_AUTHENTICATED);

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(convertObjectToJson(errorDetails));
        }
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
