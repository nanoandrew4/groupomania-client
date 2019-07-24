package com.greenapper.filters;

import com.greenapper.services.CookieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Extracts the cookie containing the user token, if it exists, and sets it for the current request in the {@link CookieService}
 * so that it can be used as part of requests made to the backend.
 */
@Component
public class CookieExtractorFilter implements Filter {

	@Autowired
	private CookieService cookieService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest servletRequest = (HttpServletRequest) request;

		if (servletRequest.getCookies() != null && servletRequest.getCookies().length > 0) {
			for (Cookie c : servletRequest.getCookies()) {
				if ("cmToken".equals(c.getName()) && c.getValue() != null)
					cookieService.setCampaignManagerToken(c.getValue());
			}
		}

		chain.doFilter(request, response);
	}
}
