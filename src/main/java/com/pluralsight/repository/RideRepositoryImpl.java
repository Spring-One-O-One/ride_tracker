package com.pluralsight.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.pluralsight.model.Ride;

@Repository("rideRepository")
public class RideRepositoryImpl implements RideRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Ride createRide(Ride ride) {
		// 2 ways to interact with database: using JdbcTemplate or SimpleJdbcInsert

		// 1: Insert using jdbcTemplate:
//		jdbcTemplate.update(
//			"INSERT INTO ride (name, duration) values (?, ?)",
//			ride.getName(),
//			ride.getDuration()
//		);
		
		// 2: Insert using SimpleJdbcInsert (In an application, could make this SimpleJdbcInsert once)
		SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate);
		
		// make columns
		List<String> columns = new ArrayList<>();
		columns.add("name");
		columns.add("duration");
		
		// map to table
		insert.setTableName("ride");
		insert.setColumnNames(columns);
		
		// insert into table
		Map<String, Object> data = new HashMap<>();
		data.put("name", ride.getName());
		data.put("duration", ride.getDuration());
		
		// Get the id of created item
		insert.setGeneratedKeyName("id");
		Number key = insert.executeAndReturnKey(data);
		System.out.println(key);

		return null;
	}
	
	@Override
	public List<Ride> getRides() {
		Ride ride = new Ride();
		ride.setName("Corner Canyon");
		ride.setDuration(120);
		List <Ride> rides = new ArrayList<>();
		rides.add(ride);
		return rides;
	}
	
}
