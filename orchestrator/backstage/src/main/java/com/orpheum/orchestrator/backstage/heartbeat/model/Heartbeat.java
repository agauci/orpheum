package com.orpheum.orchestrator.backstage.heartbeat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("heartbeat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Heartbeat {
    @Id
    Long            id;
    HeartbeatType   type;
    String          identifier;
    LocalDateTime   timestamp;
    boolean         active;
}
