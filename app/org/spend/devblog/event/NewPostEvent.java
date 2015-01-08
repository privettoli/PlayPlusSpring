package org.spend.devblog.event;

import org.spend.devblog.domain.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewPostEvent {
    private final PostEntity post;
}
