package com.thiscompany.ttrack.service.database.impl;

import com.thiscompany.ttrack.controller.user.dto.UserEntryTimestamp;
import com.thiscompany.ttrack.service.database.DataWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationEventWriter implements DataWriter<List<UserEntryTimestamp>> {
	
	private final NamedParameterJdbcTemplate jdbcTemplate;
	
	private final String SQL = "UPDATE users SET last_login = :lastLogin WHERE username = :username";
	
	@Override
	public void write(List<UserEntryTimestamp> loginSet) {
		SqlParameterSource[] dataBatch = SqlParameterSourceUtils.createBatch(loginSet.toArray());
		jdbcTemplate.batchUpdate(SQL, dataBatch);
	}
}
