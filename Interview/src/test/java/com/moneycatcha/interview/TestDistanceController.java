package com.moneycatcha.interview;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.moneycatcha.interview.controllers.InterviewController;
import com.moneycatcha.interview.controllers.integration.DistanceService;
import com.moneycatcha.interview.controllers.integration.model.DistanceResponse;
import com.moneycatcha.interview.controllers.integration.model.DistanceValue;
import com.moneycatcha.interview.controllers.integration.model.TravelMode;
import com.moneycatcha.interview.controllers.integration.model.UnitType;
@RunWith(SpringRunner.class)
@WebMvcTest
public class TestDistanceController {

	@Autowired
	ApplicationContext applicationContext;
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	DistanceService distanceService;
	
	@Test
	public void allGood() throws Exception {
		DistanceResponse response = new DistanceResponse();
		response.setDestination("Milan");
		response.setOrigin("Rome");
		response.setFound(true);
		response.setDistance(new DistanceValue("800 Km", 8000));
		response.setDuration(new DistanceValue("8 hours", 8));
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE)).willReturn(response);
		this.mockMvc.perform(get("/public/distance").param("units", "imperial").param("origin", "Rome").param("destination", "Milan"))
		.andExpect(status().isOk());
		verify(distanceService).getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE);

	}
	
	@Test
	public void expectBadRequest() throws Exception {
		
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE)).willReturn(null);
		this.mockMvc.perform(get("/public/distance").param("units", "imperial").param("origin", "Rome").param("destination", "Milan"))
		.andExpect(status().isBadRequest());
		verify(distanceService).getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE);

	}
	
	@Test public void internalError() throws Exception {
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE)).willThrow(RuntimeException.class);
		this.mockMvc.perform(get("/public/distance").param("units", "imperial").param("origin", "Rome").param("destination", "Milan"))
		.andExpect(status().isInternalServerError());
		verify(distanceService).getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE);
	}
	
	@Test
	public void notFound() throws Exception {
		DistanceResponse response = new DistanceResponse();
		response.setDestination("Milan");
		response.setOrigin("xxxxxx");
		response.setFound(false);
		response.setDistance(new DistanceValue("800 Km", 8000));
		response.setDuration(new DistanceValue("8 hours", 8));
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE)).willReturn(response);
		this.mockMvc.perform(get("/public/distance").param("units", "imperial").param("origin", "Rome").param("destination", "Milan"))
		.andExpect(status().isOk());
		verify(distanceService).getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE);

	}
	
	
	

}
