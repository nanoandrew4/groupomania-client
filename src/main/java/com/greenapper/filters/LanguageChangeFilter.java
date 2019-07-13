package com.greenapper.filters;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.net.URL;

@Component
public class LanguageChangeFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final String langParam = request.getParameter("lang");
		if (langParam != null && !langParam.isEmpty())
			new URL("http://localhost:8444/?lang=" + langParam).openConnection().connect();

		chain.doFilter(request, response);
	}
}
