package com.thiscompany.ttrack.model;

import com.thiscompany.ttrack.enums.Priority;
import com.thiscompany.ttrack.enums.TaskState;
import com.thiscompany.ttrack.enums.TaskStatus;
import com.thiscompany.ttrack.model.auditable.Auditable;
import com.thiscompany.ttrack.model.identifier_generation.strategy.IdGeneration;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;


@SuppressWarnings("deprecation")
@Getter @Setter
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(name = "task.graph", attributeNodes = @NamedAttributeNode("owner"))
public class Task extends Auditable implements IdGeneration {
	
	@Id
	@GenericGenerator(name = "custom-taskId", strategy = "com.thiscompany.ttrack.model.identifier_generation.CustomIdGenerator")
	@GeneratedValue(generator = "custom-taskId")
	@Column(updatable = false)
	@EqualsAndHashCode.Include
	private String id;
	
	@Column(nullable = false)
	private String title;
	
	@Column(length = 540)
	private String details;
	
	@Column(nullable = false)
	private Priority priority;
	
	@Column(nullable = false)
	private TaskStatus status;
	
	@Column(nullable = false, columnDefinition = "VARCHAR")
	private TaskState state;
	
	@Column(name = "is_completed", nullable = false)
	private boolean isCompleted;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "owner", referencedColumnName = "username",
		updatable = false, nullable = false
	)
	@EqualsAndHashCode.Include
	private User owner;
	
	@Override
	public String generateId() {
		return "t" + Math.abs(Objects.hash(owner, getCreatedAt()));
	}
	
	public Task setStatus(TaskStatus status) {
		this.status = status;
		return this;
	}
	
	public Task setState(TaskState state) {
		this.state = state;
		return this;
	}
	
	public boolean getIsCompleted() {
		return isCompleted;
	}
	
}
