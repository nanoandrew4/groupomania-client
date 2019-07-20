package com.greenapper.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Order(2)
public class LanguageChangeFilter implements Filter {

	@Value(value = "${groupomania.server.backend.url}")
	private String serverUrl;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final String langParam = request.getParameter("lang");
		if (langParam != null && !langParam.isEmpty())
			((HttpURLConnection) new URL(serverUrl + "?lang=" + langParam).openConnection()).getResponseCode();

		chain.doFilter(request, response);
	}
}
