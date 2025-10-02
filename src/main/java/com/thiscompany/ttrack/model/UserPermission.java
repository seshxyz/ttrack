package com.thiscompany.ttrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Setter @Getter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_permission")
public class UserPermission {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_name", referencedColumnName = "username")
	private User user_name;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "permission_id", referencedColumnName = "id")
	private Permission permission;
	
	@CreatedBy
	@Column(nullable = false, updatable = false)
	private String assignedBy;
	
	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime assignedAt;
	
}
