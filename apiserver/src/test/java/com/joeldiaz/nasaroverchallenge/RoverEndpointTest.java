package com.joeldiaz.nasaroverchallenge;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoverEndpointTest {
    
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void testGetRovers() {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/rover", String.class);
		
		assertTrue(response.getStatusCode() == HttpStatus.OK);
    }
    
	@Test
	void testBadUrl() {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/rovernonexist", String.class);
		assertTrue(response.getStatusCode() == HttpStatus.NOT_FOUND);
    }
    
	@Test
	void testGetRoversImages() {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/rover/images", String.class);
		
		assertTrue(response.getStatusCode() == HttpStatus.OK);
    }
    
	@Test
	void testGetRoverImages() {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/rover/curiosity/images", String.class);	
		assertTrue(response.getStatusCode() == HttpStatus.OK);
    }
    
	@Test
	void testGetRoverCamerasImages() {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/api/rover/curiosity/images/MAST", String.class);	
		assertTrue(response.getStatusCode() == HttpStatus.OK);
	}
}
