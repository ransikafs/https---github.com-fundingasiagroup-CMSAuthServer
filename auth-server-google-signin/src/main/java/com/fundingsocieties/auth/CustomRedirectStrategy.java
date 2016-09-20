package com.fundingsocieties.auth;

import org.springframework.http.MediaType;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.util.UrlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomRedirectStrategy extends DefaultRedirectStrategy {

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().append(prepareRedirectUrl(request.getContextPath(), url));
        response.flushBuffer();
    }

    private String prepareRedirectUrl(String contextPath, String url) {
        if (!UrlUtils.isAbsoluteUrl(url)) {
            return contextPath + url;
        }
        return url;
    }
}
