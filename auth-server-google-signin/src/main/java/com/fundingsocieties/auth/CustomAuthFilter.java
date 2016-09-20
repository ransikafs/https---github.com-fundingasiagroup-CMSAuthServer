package com.fundingsocieties.auth;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.web.client.RestTemplate;

public class CustomAuthFilter extends AbstractAuthenticationProcessingFilter {

	public CustomAuthFilter(RequestCache requestCache) {
		super("/login");
		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setRequestCache(requestCache);
		successHandler.setDefaultTargetUrl("/home");
		successHandler.setRedirectStrategy(new CustomRedirectStrategy());
		this.setAuthenticationSuccessHandler(successHandler);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {

		PlusPerson person = loadProfileFromGoogle(request.getParameter("access-token"));
		Optional<PlusEmail> accountMail = person.getEmails().stream()
				.filter(plusEmail -> plusEmail.getType().equals("account")).findAny();
		Authentication token = new UsernamePasswordAuthenticationToken(accountMail.get().getValue(), "");
		return this.getAuthenticationManager().authenticate(token);
	}

	private PlusPerson loadProfileFromGoogle(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		ResponseEntity<PlusPerson> response = new RestTemplate().exchange(
				"https://www.googleapis.com/plus/v1/people/me", HttpMethod.GET, new HttpEntity<>(headers),
				PlusPerson.class);
		return response.getBody();
	}
}
