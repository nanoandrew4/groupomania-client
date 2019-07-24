package com.greenapper.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.greenapper.dtos.ImageDTO;
import com.greenapper.dtos.ServerRequest;
import com.greenapper.dtos.ServerResponse;
import com.greenapper.services.HttpRequestHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/images")
public class ImageFetcherController {

	@Autowired
	private HttpRequestHandlerService httpRequestHandlerService;

	@GetMapping
	public @ResponseBody
	byte[] findImage(@RequestParam final String fileName) {
		final ServerRequest serverRequest = new ServerRequest();
		serverRequest.setRelativeUri("/images?fileName=" + fileName);
		serverRequest.setMethod("GET");
		serverRequest.setResponseBodyType(new TypeReference<ImageDTO>() {
		});

		final ServerResponse serverResponse = httpRequestHandlerService.sendAndHandleRequest(serverRequest, null, null);
		return ((ImageDTO) serverResponse.getBody()).getImage();
	}
}
