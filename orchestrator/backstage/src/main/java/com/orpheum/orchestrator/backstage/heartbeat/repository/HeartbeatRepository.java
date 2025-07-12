package com.orpheum.orchestrator.backstage.heartbeat.repository;

import com.orpheum.orchestrator.backstage.heartbeat.model.Heartbeat;
import org.springframework.data.repository.CrudRepository;

public interface HeartbeatRepository extends CrudRepository<Heartbeat, Long> {
}
