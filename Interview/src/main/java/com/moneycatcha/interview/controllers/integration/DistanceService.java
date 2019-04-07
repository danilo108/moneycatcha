package com.moneycatcha.interview.controllers.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.moneycatcha.interview.controllers.integration.model.DistanceResponse;
import com.moneycatcha.interview.controllers.integration.model.TravelMode;
import com.moneycatcha.interview.controllers.integration.model.UnitType;
import com.moneycatcha.interview.controllers.integration.translators.Translator;
/**
 * 
 * @author Danilo
 *
 */
@Component
public class DistanceService {
	/**
	 * the Google api Key saved into applicaion.properties
	 */
	@Value("${interview.api.google.key}")
	private String distanceAPIKey;

	/**
	 * The Google API URL to get the distance between two or more points
	 */
	@Value("${interview.api.google.url}")
	private String distanceAPIURL;

	/**
	 * This method is an abstraction of the Google API method and get the distance only between two places.
	 * Another restriction is that the Travel mode is restricted only to driving or walking
	 * 
	 * @param units imperial/metric
	 * @param origin the place of origin
	 * @param destination the place of destination
	 * @param mode (driving/walking)
	 * @return DistacneResponse an abstraction of the Google payload
	 */
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

	/**
	 * It calls the Api using rest template
	 * @param unit
	 * @param origin
	 * @param destination
	 * @param mode
	 * @return
	 */
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
