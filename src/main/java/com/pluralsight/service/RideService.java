package com.pluralsight.service;

import java.util.List;

import com.pluralsight.model.Ride;

public interface RideService {

	Ride createRide(Ride ride);
	
	Ride getRide(Integer id);
	
	List<Ride> getRides();

	Ride updateRide(Ride ride);

	void batch();
	
	void deleteRide(Integer id);

}