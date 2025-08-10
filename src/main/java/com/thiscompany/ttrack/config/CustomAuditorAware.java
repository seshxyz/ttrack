package com.thiscompany.ttrack.config;

import com.thiscompany.ttrack.model.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
       return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
               .map(auth -> {
                   if (auth.getPrincipal() instanceof User) {
                       return ((User) auth.getPrincipal()).getUsername();
                   } else if (SecurityContextHolder.getContext().getAuthentication() == null)
                       return "system";
                   else return "system";
               });
    }
}
