package com.moneycatcha.interview.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.hateoas.HateoasProperties;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
public class InterviewController {

	@Autowired
	DistanceService distanceService;

	@RequestMapping(path = "drive/distance", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> getDistancePublic(

			@RequestParam(name = "units", defaultValue = "imperial") String units,
			@RequestParam(name = "origin", required = true) String origin,
			@RequestParam(name = "destination", required = true) String destination) {
		return getDistance(units, origin, destination, TravelMode.DRIVE);
	}

	@RequestMapping(path = "walk/distance", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> getDistancePrivate(

			@RequestParam(name = "units", defaultValue = "imperial") String units,
			@RequestParam(name = "origin", required = true) String origin,
			@RequestParam(name = "destination", required = true) String destination) {
		return getDistance(units, origin, destination, TravelMode.WALK);
	}

	private ResponseEntity<String> getDistance(String units, String origin, String destination, TravelMode mode ) {
		StringBuilder sb = new StringBuilder();
		UnitType unit;
		try {
			unit = UnitType.getEnum(units);
		} catch (IllegalArgumentException e1) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		try {

			
			DistanceResponse distance = distanceService.getDistance(unit, origin, destination, mode);
			if (distance == null) {
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
			sb.append(translateResponse(mode, distance));

		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
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
			sb.append(mode.getMode());
		} else {
			sb.append("Unfortunately the origin and / or the destination could not be geo located. Try again!");

		}
		return sb.toString();
	}

}
