package com.moneycatcha.interview;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.moneycatcha.interview.controllers.integration.model.DistanceResponse;
import com.moneycatcha.interview.controllers.integration.translators.Translator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTranslator {

	
	

	private static final String NOT_OK_JSON_RESPONSE = "{\r\n" + "   \"destination_addresses\" : [],\r\n"
			+ "   \"origin_addresses\" : [],\r\n" + "   \"rows\" : [],\r\n" + "   \"status\" : \"INVALID_REQUEST\"\r\n"
			+ "}";
	private static final String NOT_FOUND = "{\r\n" + "   \"destination_addresses\" : [ \"\" ],\r\n"
			+ "   \"origin_addresses\" : [ \"\" ],\r\n" + "   \"rows\" : [\r\n" + "      {\r\n"
			+ "         \"elements\" : [\r\n" + "            {\r\n" + "               \"status\" : \"NOT_FOUND\"\r\n"
			+ "            }\r\n" + "         ]\r\n" + "      }\r\n" + "   ],\r\n" + "   \"status\" : \"OK\"\r\n" + "}";

	private static final String DESTINATION_NOT_FOUND = "{\r\n" + "   \"destination_addresses\" : [ \"\" ],\r\n"
			+ "   \"origin_addresses\" : [ \"Rome, Metropolitan City of Rome, Italy\" ],\r\n" + "   \"rows\" : [\r\n"
			+ "      {\r\n" + "         \"elements\" : [\r\n" + "            {\r\n"
			+ "               \"status\" : \"NOT_FOUND\"\r\n" + "            }\r\n" + "         ]\r\n" + "      }\r\n"
			+ "   ],\r\n" + "   \"status\" : \"OK\"\r\n" + "}";
	private static final String ORIGIN_NOT_FOUND = "{\r\n"
			+ "   \"destination_addresses\" : [ \"Rome, Metropolitan City of Rome, Italy\" ],\r\n"
			+ "   \"origin_addresses\" : [ \"\" ],\r\n" + "   \"rows\" : [\r\n" + "      {\r\n"
			+ "         \"elements\" : [\r\n" + "            {\r\n" + "               \"status\" : \"NOT_FOUND\"\r\n"
			+ "            }\r\n" + "         ]\r\n" + "      }\r\n" + "   ],\r\n" + "   \"status\" : \"OK\"\r\n" + "}";
	private static final String ALL_GOOD = "{\r\n" + "   \"destination_addresses\" : [ \"Oslo, Norway\" ],\r\n"
			+ "   \"origin_addresses\" : [ \"Rome, Metropolitan City of Rome, Italy\" ],\r\n" + "   \"rows\" : [\r\n"
			+ "      {\r\n" + "         \"elements\" : [\r\n" + "            {\r\n"
			+ "               \"distance\" : {\r\n" + "                  \"text\" : \"1,568 mi\",\r\n"
			+ "                  \"value\" : 2523751\r\n" + "               },\r\n"
			+ "               \"duration\" : {\r\n" + "                  \"text\" : \"1 day 2 hours\",\r\n"
			+ "                  \"value\" : 94709\r\n" + "               },\r\n"
			+ "               \"status\" : \"OK\"\r\n" + "            }\r\n" + "         ]\r\n" + "      }\r\n"
			+ "   ],\r\n" + "   \"status\" : \"OK\"\r\n" + "}";

	private static final String MALFORMED_RESPONSE = "{\r\n"
			+ "   \"destination_addresses\" : [ \"Oslo, Norway\" ],\r\n"
			+ "   \"origin_addresses\" : [ \"Rome, Metropolitan City of Rome, Italy\" ],\r\n" + "   \"rows\" : [\r\n"
			+ "      {\r\n" + "         \"elements\" : [\r\n" + "            {\r\n"
			+ "               \"distance\" : {\r\n" + "                  \"text\" : \"1,568 mi\",\r\n"
			+ "                  \"value\" : 252aaaa3751\r\n" + "               },\r\n"
			+ "               \"duration\" : {\r\n" + "                  \"text\" : \"1 day 2 hours\",\r\n"
			+ "                  \"value\" : 947eee09\r\n" + "               },\r\n"
			+ "               \"status\" : \"OK\"\r\n" + "            }\r\n" + "         ]\r\n" + "      }\r\n"
			+ "   ],\r\n" + "   \"status\" : \"OK\"\r\n" + "}";


	
	
	
	@Test
	public void testPlacesNOTOK() {
		DistanceResponse response = new Translator().translateJSONToDistanceResponse( NOT_OK_JSON_RESPONSE);
		assertNull(response);

	}

	@Test
	public void testPlacesNOTFound() {
		DistanceResponse response = new Translator().translateJSONToDistanceResponse( NOT_FOUND);
		assertNotNull(response);
		assertFalse(response.isFound());
		assertNull(response.getDistance());

	}

	@Test
	public void testPlacesOriginNOTFound() {
		DistanceResponse response = new Translator().translateJSONToDistanceResponse(ORIGIN_NOT_FOUND);
		assertNotNull(response);
		assertFalse(response.isFound());
		assertNotNull(response.getDestination());
		assertTrue(response.getOrigin() == null || response.getOrigin().trim().equals(""));
	}

	@Test
	public void testPlacesDestinationNOTFound() {
		DistanceResponse response = new Translator().translateJSONToDistanceResponse(DESTINATION_NOT_FOUND );
		assertNotNull(response);
		assertFalse(response.isFound());
		assertNotNull(response.getOrigin());
		assertTrue(response.getDestination() == null || response.getDestination().trim().equals(""));
	}

	@Test
	public void testPlacesAllGood() {
		DistanceResponse response = new Translator().translateJSONToDistanceResponse( ALL_GOOD);
		assertNotNull(response);
		assertTrue(response.isFound());
		assertNotNull(response.getOrigin());
		assertNotNull(response.getDestination());
		assertNotNull(response.getDistance());
		assertNotNull(response.getDuration());
	}

	

	@Test
	public void testMalFormedResponse() {
		

		DistanceResponse response = new Translator().translateJSONToDistanceResponse( MALFORMED_RESPONSE);
		assertNull(response);
	}

}
