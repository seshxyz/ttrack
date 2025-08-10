package com.thiscompany.ttrack.model;

import com.thiscompany.ttrack.enums.SystemAuthority;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Short id;

    @Column(unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private SystemAuthority name;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
    private Set<UserPermission> userAuthorities;

}
