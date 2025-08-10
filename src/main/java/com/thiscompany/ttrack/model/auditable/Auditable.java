package com.thiscompany.ttrack.model.auditable;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Getter @Setter
@MappedSuperclass
public class Auditable {

    @CreatedDate
    @JsonFormat(pattern = "dd-MM-YYYY HH:mm:ss")
    @Column(nullable = false)
    private Date createdAt;

    @CreatedBy
    @Column(nullable = false)
    private String createdBy;

    @LastModifiedDate
    @JsonFormat(pattern = "dd-MM-YYYY HH:mm:ss")
    private Date updatedAt;

    @LastModifiedBy
    private String updatedBy;
}
