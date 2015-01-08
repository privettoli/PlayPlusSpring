package org.spend.devblog.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints.Required;

import javax.persistence.*;

import static javax.persistence.InheritanceType.JOINED;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
@Inheritance(strategy = JOINED)
@EqualsAndHashCode(callSuper = true)
public class PostEntity extends AbstractEntity {
    @Required
    private String message;
    private Boolean isCode;
}
