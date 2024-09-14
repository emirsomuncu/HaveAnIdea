package com.emirsomuncu.HaveAnIdea.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        Boolean isUser = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));
        Boolean isAdmin = authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isUser && isAdmin) {
            setDefaultTargetUrl("/admin/admin-selection-page"); // bu sayfada iki seçim olsun admin gibi mi user gibi mi davranmak istiyosun diye admin birini seçsin . ona göre bi urlye yolla .
        }
        else if(isAdmin) {
            setDefaultTargetUrl("/admin/home"); // direkt admin pageine at
        }
        else if(isUser) {
            setDefaultTargetUrl("/user/home");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
