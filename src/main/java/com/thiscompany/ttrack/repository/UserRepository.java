package com.thiscompany.ttrack.repository;

import com.thiscompany.ttrack.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
	
	@EntityGraph(value = "user.graph")
	Optional<User> findUserByUsername(String username);
	
	@EntityGraph(value = "user.graph")
	Optional<User> findUserById(String id);
	
}
