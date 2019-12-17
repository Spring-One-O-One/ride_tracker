package com.pluralsight.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.pluralsight.model.Ride;
import com.pluralsight.repository.util.RideRowMapper;

@Repository("rideRepository")
public class RideRepositoryImpl implements RideRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	// Create method using jdbcTemplate:
//	@Override Commented out because no longer named "createRide"
	public Ride createRideUsingJdbcTemplate(Ride ride) {
		// In order to return the newly created ride with its generated id:
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
					"INSERT INTO ride (name, duration) values (?, ?)",
					new String[] {"id"}
				);

				ps.setString(1, ride.getName());
				ps.setInt(2, ride.getDuration());

				return ps;
			}
		}, keyHolder);

		Number id = keyHolder.getKey();

		return getRide(id.intValue());
	}

	@Override
	public Ride getRide(Integer id) {
		Ride ride = jdbcTemplate.queryForObject(
			"SELECT * FROM ride WHERE id = ?",
			new RideRowMapper(),
			id
		);
		
		return ride;
	}
	
	// Create method using SimpleJdbcInsert
	@Override
	public Ride createRide(Ride ride) {
		// In an application, could make this SimpleJdbcInsert once
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
		Number id = insert.executeAndReturnKey(data);

		return getRide(id.intValue());
	}

	@Override
	public List<Ride> getRides() {
		List<Ride> rides = jdbcTemplate.query(
			"select * from ride",
			new RideRowMapper()
		);

		return rides;
	}
	
	@Override
	public Ride updateRide(Ride ride) {
		String SQL = "UPDATE ride SET name = ?, duration = ? WHERE id = ?";

		jdbcTemplate.update(
				SQL,
				ride.getName(),
				ride.getDuration(),
				ride.getId()
		);

		return ride;
	}
	
	// JdbcTemplate.batchUpdate for updating/inserting multiple items in DB
	@Override
	public void updateRides(List<Object[]> pairs) {
		// "pairs" Order matters [Date, Id] due to SQL String:
		String SQL = "UPDATE ride SET ride_date = ? WHERE id = ?";

		jdbcTemplate.batchUpdate(SQL, pairs);
	}
	
}
