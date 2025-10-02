package com.thiscompany.ttrack.service.authentication.entry_logging;

import com.thiscompany.ttrack.controller.user.dto.UserAuthDate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAuthenticationWriter {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL = "UPDATE users SET last_login = ? WHERE username = ?";

    public void flushDataToDatabase(final List<UserAuthDate> loginSet, final int BATCH_SIZE) {
        jdbcTemplate.batchUpdate(SQL, loginSet, BATCH_SIZE, (statement, entry)-> {
            statement.setTimestamp(1, Timestamp.valueOf(entry.lastLogin()));
            statement.setString(2, entry.username());
        });
    }

}
