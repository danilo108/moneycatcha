package com.moneycatcha.interview;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.moneycatcha.interview.controllers.integration.DistanceService;
import com.moneycatcha.interview.controllers.integration.model.DistanceResponse;
import com.moneycatcha.interview.controllers.integration.model.DistanceValue;
import com.moneycatcha.interview.controllers.integration.model.TravelMode;
import com.moneycatcha.interview.controllers.integration.model.UnitType;

@RunWith(SpringRunner.class)
@WebMvcTest()
public class TestDistanceController {

	@Autowired
    private WebApplicationContext context;
 
    private MockMvc mockMvc;
 
    @Before
    public void setup() {
    	mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .apply(springSecurity())
          .build();
    }

	
	@MockBean
	DistanceService distanceService;
	
	@Test
	public void testStrinResultWithNotFound() throws Exception {
		DistanceResponse response = new DistanceResponse();
		response.setDestination("Milan");
		response.setOrigin("Rome");
		response.setFound(false);
		response.setDistance(new DistanceValue("800 Km", 8000));
		response.setDuration(new DistanceValue("8 hours", 8));
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE))
				.willReturn(response);
		this.mockMvc.perform(
				get("/drive/distance").param("units", "imperial").param("origin", "Rome").param("destination", "Milan"))
				.andExpect(status().isOk()).andExpect(content().string("Unfortunately the origin and / or the destination could not be geo located. Try again!"));
		verify(distanceService).getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE);
	
	}
	@Test
	public void testStrinResultWithFound() throws Exception {
		DistanceResponse response = new DistanceResponse();
		response.setDestination("Milan");
		response.setOrigin("Rome");
		response.setFound(true);
		response.setDistance(new DistanceValue("800 Km", 8000));
		response.setDuration(new DistanceValue("8 hours", 8));
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE))
				.willReturn(response);
		this.mockMvc.perform(
				get("/drive/distance").param("units", "imperial").param("origin", "Rome").param("destination", "Milan"))
				.andExpect(status().isOk()).andExpect(content().string("The distance between Rome and Milan is 800 Km and it will take 8 hours driving"));
		verify(distanceService).getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE);
	
	}

	@Test
	public void allGood() throws Exception {
		DistanceResponse response = new DistanceResponse();
		response.setDestination("Milan");
		response.setOrigin("Rome");
		response.setFound(true);
		response.setDistance(new DistanceValue("800 Km", 8000));
		response.setDuration(new DistanceValue("8 hours", 8));
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE))
				.willReturn(response);
		this.mockMvc.perform(
				get("/drive/distance").param("units", "imperial").param("origin", "Rome").param("destination", "Milan"))
				.andExpect(status().isOk());
		verify(distanceService).getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE);
	
	}
	
	@Test
	public void expectBadRequest() throws Exception {
	
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE))
				.willReturn(null);
		this.mockMvc.perform(
				get("/drive/distance").param("units", "imperial").param("origin", "Rome").param("destination", "Milan"))
				.andExpect(status().isBadRequest());
		verify(distanceService).getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE);
	
	}
	
	@Test
	public void internalError() throws Exception {
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE))
				.willThrow(RuntimeException.class);
		this.mockMvc.perform(
				get("/drive/distance").param("units", "imperial").param("origin", "Rome").param("destination", "Milan"))
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
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE))
				.willReturn(response);
		this.mockMvc.perform(
				get("/drive/distance")
				.param("units", "imperial")
				.param("origin", "Rome")
				.param("destination", "Milan")
					.with(user("user").password("secret").roles("USER"))
				)
				.andExpect(status().isOk());
		verify(distanceService).getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE);
	
	}
	
	@Test
	public void wrongRoleForPrivateAccess() throws Exception {
		DistanceResponse response = new DistanceResponse();
		response.setDestination("Genoa");
		response.setOrigin("Rome");
		response.setFound(true);
		response.setDistance(new DistanceValue("800 Km", 8000));
		response.setDuration(new DistanceValue("8 hours", 8));
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Rome", "Genoa", TravelMode.DRIVE))
				.willReturn(response);
		String encriptedPassword = new BCryptPasswordEncoder().encode("secret");
		this.mockMvc
				.perform(get("/walk/distance").param("units", "imperial").param("origin", "Rome")
						.param("destination", "Milan").with(user("user").password("secret").roles("NOT_USER")))
				.andExpect(status().isForbidden());
	
	}
		
	@Test
	public void goodRoleForPrivateAccess() throws Exception {
		DistanceResponse response = new DistanceResponse();
		response.setDestination("Sydney");
		response.setOrigin("Brisbane");
		response.setFound(true);
		response.setDistance(new DistanceValue("800 Km", 8000));
		response.setDuration(new DistanceValue("8 hours", 8));
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Brisbane", "Sydney", TravelMode.DRIVE))
		
				.willReturn(response);
		this.mockMvc
				.perform(get("/walk/distance").param("units", "imperial").param("origin", "Brisbane")
						.param("destination", "Sydney")
						.with(user("user").password("secret").roles("USER"))
						)
				.andExpect(status().isOk());
		verify(distanceService).getDistance(UnitType.IMPERIAL, "Brisbane", "Sydney", TravelMode.DRIVE);

	}
	
	@Test
	public void wrongCredentialsForPrivateAccess() throws Exception {
		DistanceResponse response = new DistanceResponse();
		response.setDestination("Milan");
		response.setOrigin("Rome");
		response.setFound(true);
		response.setDistance(new DistanceValue("800 Km", 8000));
		response.setDuration(new DistanceValue("8 hours", 8));
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE))
		
				.willReturn(response);
		this.mockMvc
				.perform(get("/drive/distance").param("units", "imperial").param("origin", "Rome")
						.param("destination", "Milan")
						.with(user("user").password("seeeeeecret").roles("AAUSER"))
						.with(httpBasic("user", "aaaaasecret"))
						)
				.andExpect(status().isOk());

	}


	@Test
	public void noAUthenticationForPrivateAccess() throws Exception {
		DistanceResponse response = new DistanceResponse();
		response.setDestination("Milan");
		response.setOrigin("Rome");
		response.setFound(true);
		response.setDistance(new DistanceValue("800 Km", 8000));
		response.setDuration(new DistanceValue("8 hours", 8));
		BDDMockito.given(distanceService.getDistance(UnitType.IMPERIAL, "Rome", "Milan", TravelMode.DRIVE))
				.willReturn(response);
		this.mockMvc.perform(
				get("/walk/somethingdistance").param("units", "imperial").param("origin", "Rome").param("destination", "Milan"))
				.andExpect(status().isUnauthorized());

	}

}
