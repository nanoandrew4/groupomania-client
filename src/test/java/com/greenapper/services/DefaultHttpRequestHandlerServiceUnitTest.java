package com.greenapper.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenapper.dtos.CampaignManagerProfileDTO;
import com.greenapper.dtos.ServerRequest;
import com.greenapper.dtos.ServerResponse;
import com.greenapper.dtos.campaign.OfferCampaignDTO;
import com.greenapper.forms.campaigns.OfferCampaignForm;
import com.greenapper.services.impl.DefaultHttpRequestHandlerService;
import com.greenapper.services.impl.HttpRequestService;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;

import java.util.HashMap;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultHttpRequestHandlerServiceUnitTest {
	@InjectMocks
	private HttpRequestHandlerService httpRequestHandlerService = new DefaultHttpRequestHandlerService();

	@Mock
	private HttpRequestService httpRequestService;

	@Spy
	private ObjectMapper objectMapper;

	@Before
	public void setup() {
		ReflectionTestUtils.setField(httpRequestHandlerService, "serverUrl", "http://localhost/api");
	}

	@Test
	public void sendAndHandleRequest_makeRequestWithNoReqBodyAndRespBody_returnServerResponseWithCode200() {
		final String body = "{\"id\":1,\"name\":\"TestName\",\"email\":\"test@test.com\",\"address\":\"Test address, imaginary city, imaginary country\"}";
		final ServerResponse mockedServerResponse = new ServerResponse(200, null, body, null);

		when(httpRequestService.sendRequest(anyString(), anyString(), anyMap(), nullable(String.class))).thenReturn(mockedServerResponse);

		final ServerRequest serverRequest = createSampleRequest();
		serverRequest.setResponseBodyType(new TypeReference<CampaignManagerProfileDTO>() {
		});

		final ServerResponse response = httpRequestHandlerService.sendAndHandleRequest(serverRequest, null, null);

		assertNotNull(response);
		assertEquals(200, response.getCode());
		assertEquals("success", response.getRedirectUri());
		assertTrue(response.getBody() instanceof CampaignManagerProfileDTO);
	}

	@Test
	public void sendAndHandleRequest_makeRequestWithReqBody_returnServerResponseWithCode200() {
		final ServerResponse mockedServerResponse = new ServerResponse(200, null, null, null);

		when(httpRequestService.sendRequest(anyString(), anyString(), anyMap(), nullable(String.class))).thenReturn(mockedServerResponse);

		final ServerRequest serverRequest = createSampleRequest();
		final ServerResponse response = httpRequestHandlerService.sendAndHandleRequest(serverRequest, new OfferCampaignDTO(), null);

		assertNotNull(response);
		assertEquals(200, response.getCode());
		assertEquals("success", response.getRedirectUri());
		assertNotNull(serverRequest.getBody());
	}

	@Test
	public void sendAndHandleRequest_makeRequestAndRedirect_returnServerResponseWithCode301() {
		final HashMap<String, List<String>> headers = new HashMap<>();
		headers.put("Location", Lists.newArrayList("/sampleredirect"));
		final ServerResponse mockedServerResponse = new ServerResponse(301, headers, null, null);

		when(httpRequestService.sendRequest(anyString(), anyString(), anyMap(), nullable(String.class))).thenReturn(mockedServerResponse);

		final ServerRequest serverRequest = createSampleRequest();
		final ServerResponse response = httpRequestHandlerService.sendAndHandleRequest(serverRequest, null, null);

		assertNotNull(response);
		assertEquals(301, response.getCode());
		assertEquals(1, response.getHeaders().size());
		assertNotNull(response.getHeaders().get("Location"));
		assertEquals("redirect:/sampleredirect", response.getRedirectUri());
	}

	@Test
	public void sendAndHandleRequest_makeUnauthenticatedRequest_returnServerResponseWithCode401() {
		final ServerResponse mockedServerResponse = new ServerResponse(401, null, null, null);

		when(httpRequestService.sendRequest(anyString(), anyString(), anyMap(), nullable(String.class))).thenReturn(mockedServerResponse);

		final ServerRequest serverRequest = createSampleRequest();
		final ServerResponse response = httpRequestHandlerService.sendAndHandleRequest(serverRequest, null, null);

		assertNotNull(response);
		assertEquals(401, response.getCode());
		assertEquals("redirect:/login", response.getRedirectUri());
	}

	@Test
	public void sendAndHandleRequest_makeRequestToNonExistentEndpoint_returnServerResponseWithCode404() {
		final ServerResponse mockedServerResponse = new ServerResponse(404, null, null, null);

		when(httpRequestService.sendRequest(anyString(), anyString(), anyMap(), nullable(String.class))).thenReturn(mockedServerResponse);

		final ServerRequest serverRequest = createSampleRequest();
		final ServerResponse response = httpRequestHandlerService.sendAndHandleRequest(serverRequest, null, null);

		assertNotNull(response);
		assertEquals(404, response.getCode());
		assertEquals("error-not-found", response.getRedirectUri());
	}

	@Test
	public void sendAndHandleRequest_makeRequestThatReturnsValidationErrors_returnServerResponseWithCode400AndValidationErrors() {
		final String validationErrorsStr = "{\"exception\":\"SampleException\",\"message\":\"This is an exception message\"," +
										   "\"validationErrors\":[\"This is a sample validation error message\"]}";
		final ServerResponse mockedServerResponse = new ServerResponse(400, null, validationErrorsStr, null);

		when(httpRequestService.sendRequest(anyString(), anyString(), anyMap(), nullable(String.class))).thenReturn(mockedServerResponse);

		final ServerRequest serverRequest = createSampleRequest();

		final OfferCampaignForm offerCampaignForm = new OfferCampaignForm();
		final BeanPropertyBindingResult errors = new BeanPropertyBindingResult(offerCampaignForm, "offerCampaignForm");
		final ServerResponse response = httpRequestHandlerService.sendAndHandleRequest(serverRequest, offerCampaignForm, errors);

		assertNotNull(response);
		assertEquals(400, response.getCode());
		assertEquals("error", response.getRedirectUri());
		assertEquals(1, errors.getAllErrors().size());
	}

	private ServerRequest createSampleRequest() {
		final ServerRequest serverRequest = new ServerRequest();
		serverRequest.setSuccessRedirectUri("success");
		serverRequest.setErrorRedirectUri("error");
		serverRequest.setMethod("GET");
		serverRequest.setRelativeUri("sampleEndpoint");
		serverRequest.setRequestParameters(new HashMap<>());
		return serverRequest;
	}
}
