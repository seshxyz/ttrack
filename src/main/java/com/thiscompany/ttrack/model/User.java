package com.thiscompany.ttrack.model;

import com.thiscompany.ttrack.model.identifier_generation.strategy.IdGeneration;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
@Entity @Getter @Setter
@Accessors(chain = true)
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"id","password"})
@NamedEntityGraph(name = "user.graph",
        attributeNodes = @NamedAttributeNode(value = "permissions", subgraph = "permission"),
        subgraphs = @NamedSubgraph(name = "permission", attributeNodes = @NamedAttributeNode("permission"))
)
public class User implements UserDetails, IdGeneration {

    @Id
    @GenericGenerator(
        name = "custom-userId",
        strategy = "com.thiscompany.ttrack.model.identifier_generation.CustomIdGenerator"
    )
    @GeneratedValue(generator = "custom-userId")
    @Column(updatable = false)
    @EqualsAndHashCode.Include
    private String id;

    @Column(nullable = false, updatable = false, unique = true, length = 120)
    @EqualsAndHashCode.Include
    private String username;

    @Column(nullable = false)
    private String password;

    @ColumnDefault("true")
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Override
    public Set<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(authority -> new SimpleGrantedAuthority(
                        authority.getPermission().getName().toString())
                )
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @OneToMany(
        mappedBy = "user_name",
        cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
        fetch = FetchType.LAZY
    )
    private Set<UserPermission> permissions = new HashSet<>();

    @Override
    public String generateId() {
        return "usr-" + Objects.hash(id, username);
    }

}
