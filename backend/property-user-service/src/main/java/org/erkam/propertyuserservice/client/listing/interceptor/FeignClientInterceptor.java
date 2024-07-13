package org.erkam.propertyuserservice.client.listing.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.erkam.propertyuserservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignClientInterceptor implements RequestInterceptor {

    private final JwtService jwtService;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String jwtToken = getJwtFromSecurityContext();
        if (jwtToken != null) {
            requestTemplate.header("Authorization", "Bearer " + jwtToken);
        }
    }

    private String getJwtFromSecurityContext() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return jwtService.generateToken(userDetails);
        }
        return null;
    }
}
