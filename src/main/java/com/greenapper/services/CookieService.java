package com.greenapper.services;

/**
 * Service that stores cookies for the duration of a request, so that authentication headers can be composed
 * using the tokens stored as cookies.
 */
public interface CookieService {
	String getCampaignManagerToken();

	void setCampaignManagerToken(final String token);
}
