package com.moneycatcha.interview.controllers.integration.translators;

import org.json.JSONException;
import org.json.JSONObject;

import com.moneycatcha.interview.controllers.integration.model.DistanceResponse;
import com.moneycatcha.interview.controllers.integration.model.DistanceValue;

public class Translator {

	public DistanceResponse translateJSONToDistanceResponse(String jsonResponse) {
		try {
			DistanceResponse response = null;
			JSONObject json = new JSONObject(jsonResponse);
			String status = json.getString("status");
			if (!"OK".equals(status)) {
				// Anything different from OK will be consider an error
				return null;
			}
			if (json.getJSONArray("rows").length() == 0 || json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").length() == 0) {
				// if there are no rows or no elements in the row, it will be consider an error
				// for malformed request
				return null;
			}
			JSONObject jsonDistance = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
			status = jsonDistance.getString("status");
			DistanceValue duration = null;
			DistanceValue distance = null;
			if ("OK".equals(status)) {
				String description = jsonDistance.getJSONObject("duration").getString("text");
				long value = jsonDistance.getJSONObject("duration").getLong("value");
				duration = new DistanceValue(description, value);
				description = jsonDistance.getJSONObject("duration").getString("text");
				value = jsonDistance.getJSONObject("duration").getLong("value");
				distance = new DistanceValue(description, value);
			}

			String originResult = "";
			if (json.getJSONArray("origin_addresses").length() > 0) {
				originResult = json.getJSONArray("origin_addresses").getString(0);
			}
			String destinationResult = "";
			if (json.getJSONArray("destination_addresses").length() > 0) {
				destinationResult = json.getJSONArray("destination_addresses").getString(0);
			}

			response = new DistanceResponse();
			response.setDestination(destinationResult);
			response.setOrigin(originResult);
			response.setDistance(distance);
			response.setDuration(duration);
			response.setFound(!("ZERO_RESULTS".equals(status) || "NOT_FOUND".equals(status)));
			response.setOriginalJson(jsonResponse);
			return response;
		} catch (JSONException e) {
			// If there is an exception of the format is consider an internal error status
			// 500
			return null;
		}

	}
}
