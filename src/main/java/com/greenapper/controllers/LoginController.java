package com.greenapper.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.greenapper.dtos.ServerResponse;
import com.greenapper.forms.LoginForm;
import com.greenapper.services.HttpRequestService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@Controller
public class LoginController {

	private final static Logger LOG = LoggerFactory.getLogger(LoginController.class);

	private final static String basicUsernameAndPassword = "public_campaign_manager_client:publicCampaignManagerSecret";

	@Autowired
	private HttpRequestService httpRequestService;

	@GetMapping("/login")
	public String login(final LoginForm loginForm) {
		return "login";
	}

	@PostMapping("/login")
	public String login(final LoginForm loginForm, final Model model, final HttpServletResponse response) {
		final HashMap<String, String> requestProperty = new HashMap<>();
		final String basicAuth = "Basic " + new String(new Base64().encode(basicUsernameAndPassword.getBytes()));
		requestProperty.put("Authorization", basicAuth);

		String params = "username=" + loginForm.getUsername() +
						"&password=" + loginForm.getPassword() +
						"&grant_type=password";
		final ServerResponse serverResponse = httpRequestService.sendRequest("/oauth/token", "POST",
																			 requestProperty, params);

		if (serverResponse.getCode() == 200) {
			response.addCookie(createTokenCookie(serverResponse.getBody()));
			return "redirect:/";
		} else {
			model.addAttribute("loginError", true);
			return "login";
		}
	}

	@GetMapping("/login-check")
	public boolean isLoggedIn(final HttpServletRequest request) {
		if (request.getCookies() != null && request.getCookies().length > 0) {
			for (Cookie c : request.getCookies())
				if ("cmToken".equals(c.getName()))
					return true;
		}
		return false;
	}

	@PostMapping("/signout")
	public String logout(final HttpServletRequest request, final HttpServletResponse response) {
		final Cookie tokenCookie;

		if (request.getCookies() != null && request.getCookies().length > 0) {
			for (Cookie c : request.getCookies()) {
				if (c.getName().contains("Token")) {
					tokenCookie = c;
					tokenCookie.setMaxAge(0);
					response.addCookie(tokenCookie);
					break;
				}
			}
		}

		return "redirect:/";
	}

	private Cookie createTokenCookie(final String oauthResponse) {
		try {
			final JsonNode rootNode = httpRequestService.getObjectMapper().readTree(oauthResponse);

			final Cookie cookie = new Cookie("cmToken", rootNode.get("access_token").asText());

			cookie.setMaxAge(rootNode.get("expires_in").asInt());
			return cookie;

		} catch (IOException e) {
			LOG.error("Error occurred while creating cookie for oauth response: " + oauthResponse);
		}

		return null;
	}
}
