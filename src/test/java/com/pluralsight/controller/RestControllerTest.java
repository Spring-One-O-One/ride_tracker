package com.pluralsight.controller;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.pluralsight.model.Ride;

import org.junit.Test;

public class RestControllerTest {

	@Test(timeout=3000)
	public void testCreateRide() {
		RestTemplate restTemplate = new RestTemplate();
		
		Ride ride = new Ride();
		ride.setName("Yellow Fork Trail");
		ride.setDuration(38);

		restTemplate.postForObject(
			"http://localhost:8080/ride_tracker/ride",
			 ride,
			 Ride.class
		 );
		
		System.out.println("Ride: " + ride);
	}
	
	@Test(timeout=3000)
	public void testGetRides() {
		RestTemplate restTemplate = new RestTemplate();
		String getRidesUri = "http://localhost:8080/ride_tracker/rides";

		ResponseEntity<List<Ride>> ridesResponse = restTemplate.exchange(
			getRidesUri,
			HttpMethod.GET,
			null,
			new ParameterizedTypeReference<List<Ride>>() {}
		);

		List<Ride> rides = ridesResponse.getBody();

		for (Ride ride : rides) {
			System.out.println("Ride name: " + ride.getName());
		}
	}
	
	@Test(timeout=3000)
	public void testGetRide() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/ride_tracker/ride/1";
		
		Ride ride = restTemplate.getForObject(url, Ride.class);
		
		System.out.println("Ride name: " + ride.getName());
	}
	
	@Test(timeout=3000)
	public void testUpdateRide() {
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/ride_tracker/ride";
		String idEndpoint = "/1";
		
		// Get a ride
		Ride ride = restTemplate.getForObject(url + idEndpoint, Ride.class);
		
		// Update the duration of that ride
		ride.setDuration(ride.getDuration() + 1);
		
		// Update it in the DB with new duration value
		restTemplate.put(url, ride);
		
		System.out.println("Ride name: " + ride.getName());
	}
}
