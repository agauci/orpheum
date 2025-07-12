package com.orpheum.orchestrator.backstage.heartbeat.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("heartbeat")
@Data
@NoArgsConstructor
public class Heartbeat {
    @Id
    Long    id;
}
