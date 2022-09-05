package com.medicow.model.entity;


import com.medicow.model.constant.ActiveStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter @Setter @ToString @EntityListeners(value={AuditingEntityListener.class})
@MappedSuperclass
public abstract class BaseEntity extends BaseTimeEntity {
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;
}
