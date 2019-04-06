package com.moneycatcha.interview.controllers.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.moneycatcha.interview.controllers.integration.model.DistanceResponse;
import com.moneycatcha.interview.controllers.integration.model.TravelMode;
import com.moneycatcha.interview.controllers.integration.model.UnitType;
import com.moneycatcha.interview.controllers.integration.translators.Translator;

@Component
public class DistanceService {
	
	@Value("${interview.api.google.key}")
	private String distanceAPIKey;

	@Value("${interview.api.google.url}")
	private String distanceAPIURL;

	
	public DistanceResponse getDistance(UnitType unit, String origin, String destination, TravelMode mode) {

		if (null == mode) {
			mode = TravelMode.DRIVE;
		}
		if (null == unit) {
			unit = UnitType.METRIC;
		}
		if (null == origin) {
			origin = "";
		}
		if (null == destination) {
			destination = "";
		}

		String jsonResponse = executeGetDistanceRequest(unit, origin, destination, mode);

		return new Translator().translateJSONToDistanceResponse(jsonResponse);

	}

	private String executeGetDistanceRequest(UnitType unit, String origin, String destination, TravelMode mode) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(distanceAPIURL)
				.queryParam("units", unit.getUnit()).queryParam("mode", mode.getMode()).queryParam("origins", origin)
				.queryParam("destinations", destination)
				.queryParam("key", distanceAPIKey);
		RestTemplate template = new RestTemplate();
		String jsonResponse = template.getForObject(builder.toUriString(), String.class);
		return jsonResponse;
	}
	
	

	
	
	
}
