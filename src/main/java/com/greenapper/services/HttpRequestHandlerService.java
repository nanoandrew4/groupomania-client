package com.greenapper.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenapper.dtos.ServerRequest;
import com.greenapper.dtos.ServerResponse;
import org.springframework.validation.Errors;

import java.util.Map;

/**
 * Makes use of the {@link com.greenapper.services.impl.HttpRequestService}. Implementations of this class
 * handle interpretation of the server responses, so that the controllers can easily fetch the necessary data from the
 * response, or redirect to the correct place.
 */
public interface HttpRequestHandlerService {
	/**
	 * Sends a request through {@link com.greenapper.services.impl.HttpRequestService#sendRequest(String, String, Map, String)}
	 * and handles the response. If a body is supplied to this method, it will be serialized with Jackson and sent over
	 * the wire as the body for the request. 200, 300 and 400 response classes are handled by this method, so that
	 * the {@link ServerResponse} returned by this method contains all the necessary data so that the caller can determine
	 * what redirections are necessary, what data should be returned, and so on. {@link ServerResponse} instances
	 * returned by this method are considered complete, and ready for interpretation.
	 *
	 * @param serverRequest The request to submit to the groupomania backend
	 * @param body          Body of the request, if any. Will be converted to a string using Jackson
	 * @param errors        Errors instance associated with the body instance, necessary for populating validation errors
	 * @return {@link ServerResponse} containing all the necessary data to interpret the server response easily
	 */
	ServerResponse sendAndHandleRequest(final ServerRequest serverRequest, final Object body, final Errors errors);

	/**
	 * Parses the body of a server response, and converts it to the desired type.
	 *
	 * @param typeReference Type to convert the parsed body to
	 * @param body          String representation of the body, which will be deserialized by Jackson
	 * @return The deserialized object, or null if deserialization failed
	 */
	Object parseBody(final TypeReference typeReference, final String body);

	ObjectMapper getObjectMapper();
}
