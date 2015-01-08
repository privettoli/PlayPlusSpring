package org.spend.devblog.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CloseConnectionEvent {
    private final String uuid;
}
