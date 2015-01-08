package org.spend.devblog.domain;

import org.spend.devblog.converter.DateConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected Long id;
    @CreatedDate
    @Convert(converter = DateConverter.class)
    protected ZonedDateTime createdDate;
    @LastModifiedDate
    @Convert(converter = DateConverter.class)
    protected ZonedDateTime modifiedDate;
}
