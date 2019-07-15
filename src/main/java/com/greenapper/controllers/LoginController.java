package com.greenapper.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenapper.forms.LoginForm;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Controller
public class LoginController {

	private final static Logger LOG = LoggerFactory.getLogger(LoginController.class);

	private final static String basicUsernameAndPassword = "public_campaign_manager_client:publicCampaignManagerSecret";

	private final static ObjectMapper objectMapper = new ObjectMapper();

	@GetMapping("/login")
	public String login(final LoginForm loginForm) {
		return "login";
	}

	@PostMapping("/login")
	public String login(final LoginForm loginForm, final Model model, final HttpServletResponse response) {
		final String loginUrl = "http://localhost:8444/api/oauth/token";
		final StringBuilder params = new StringBuilder();
		params.append("username=").append(loginForm.getUsername());
		params.append("&password=").append(loginForm.getPassword());
		params.append("&grant_type=password");

		try {
			final URL url = new URL(loginUrl);
			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");

			final String basicAuth = "Basic " + new String(new Base64().encode(basicUsernameAndPassword.getBytes()));
			conn.setRequestProperty("Authorization", basicAuth);
			conn.setDoOutput(true);
			final OutputStream os = conn.getOutputStream();
			os.write(params.toString().getBytes());
			os.flush();
			os.close();

			if (conn.getResponseCode() == 200) {
				final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				final StringBuilder responseBuffer = new StringBuilder();

				String line;
				while ((line = in.readLine()) != null)
					responseBuffer.append(line);

				response.addCookie(createTokenCookie(responseBuffer.toString()));
				return "redirect:/";
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		model.addAttribute("loginError", true);
		return "login";
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
			final JsonNode rootNode = objectMapper.readTree(oauthResponse);

			final Cookie cookie = new Cookie("cmToken", rootNode.get("access_token").asText());

			cookie.setMaxAge(rootNode.get("expires_in").asInt());
			return cookie;

		} catch (IOException e) {
			LOG.error("Error occurred while creating cookie for oauth response: " + oauthResponse);
		}

		return null;
	}
}
