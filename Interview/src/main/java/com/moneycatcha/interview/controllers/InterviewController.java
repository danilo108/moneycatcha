package com.moneycatcha.interview.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.hateoas.HateoasProperties;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.moneycatcha.interview.controllers.integration.DistanceService;
import com.moneycatcha.interview.controllers.integration.model.DistanceResponse;
import com.moneycatcha.interview.controllers.integration.model.TravelMode;
import com.moneycatcha.interview.controllers.integration.model.UnitType;
/**
 * 
 * @author Danilo
 *
 */
@RestController
public class InterviewController {

	/**
	 * The service used to get the distance information
	 */
	@Autowired
	DistanceService distanceService;

	/**
	 * Get the distance from origin to destination. Measure the disance in unit (imperial/metric) and calculate the time
	 * walking. The method is restricted to the role USER
	 * The method relies on DestinationService which return a result object that gets translated into a text string
	 * @param units imperial/metric
	 * @param origin the place of origin
	 * @param destination the place of destination
	 * @param mode (driving/walking)
	 * @return a text string describing distance and duration of the trip
	 */
	@RequestMapping(path = "drive/distance", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> getDistancePublic(

			@RequestParam(name = "units", defaultValue = "imperial") String units,
			@RequestParam(name = "origin", required = true) String origin,
			@RequestParam(name = "destination", required = true) String destination) {
		return getDistance(units, origin, destination, TravelMode.DRIVE);
	}

	/**
	 * Get the distance from origin to destination. Measure the disance in unit (imperial/metric) and calculate the time
	 * walking. The method is restricted to the role USER
	 * The method relies on DestinationService which return a result object that gets translated into a text string
	 * @param units imperial/metric
	 * @param origin the place of origin
	 * @param destination the place of destination
	 * @param mode (driving/walking)
	 * @return a text string describing distance and duration of the trip
	 */

	@RequestMapping(path = "walk/distance", method = RequestMethod.GET)
	@Secured("USER")
	public @ResponseBody ResponseEntity<String> getDistancePrivate(

			@RequestParam(name = "units", defaultValue = "imperial") String units,
			@RequestParam(name = "origin", required = true) String origin,
			@RequestParam(name = "destination", required = true) String destination) {
		return getDistance(units, origin, destination, TravelMode.WALK);
	}

	/**
	 * Get the distance from origin to destination. Measure the disance in unit (imperial/metric) and calculate the time
	 * depending on the mode (driving/walking)
	 * The method relies on DestinationService which return a result object that gets translated into a text string
	 * @param units imperial/metric
	 * @param origin the place of origin
	 * @param destination the place of destination
	 * @param mode (driving/walking)
	 * @return a text string describing distance and duration of the trip
	 */
	private ResponseEntity<String> getDistance(String units, String origin, String destination, TravelMode mode ) {
		StringBuilder sb = new StringBuilder();
		UnitType unit;
		try {
			unit = UnitType.getEnum(units);
		} catch (IllegalArgumentException e1) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		try {

			
			DistanceResponse distance = distanceService.getDistance(unit, origin, destination, mode);
			if (distance == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			sb.append(translateResponse(mode, distance));

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(sb.toString(), HttpStatus.OK);
	}

	private String translateResponse(TravelMode mode, DistanceResponse distance) {
		StringBuilder sb = new StringBuilder();
		if (distance.isFound()) {
			sb.append("The distance between ");
			sb.append(distance.getOrigin());
			sb.append(" and ");
			sb.append(distance.getDestination());
			sb.append(" is ");
			sb.append(distance.getDistance().getFormatted());
			sb.append(" and it will take ");
			sb.append(distance.getDuration().getFormatted());
			sb.append(" ");
			sb.append(mode.getMode());
		} else {
			sb.append("Unfortunately the origin and / or the destination could not be geo located. Try again!");

		}
		return sb.toString();
	}

}
