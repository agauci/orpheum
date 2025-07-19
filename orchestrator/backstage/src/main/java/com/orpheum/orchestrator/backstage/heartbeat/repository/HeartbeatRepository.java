package com.orpheum.orchestrator.backstage.heartbeat.repository;

import com.orpheum.orchestrator.backstage.heartbeat.model.Heartbeat;
import com.orpheum.orchestrator.backstage.heartbeat.model.HeartbeatType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HeartbeatRepository extends CrudRepository<Heartbeat, Long> {

    void deleteHeartbeatByTypeAndIdentifier(HeartbeatType type, String identifier);

    List<Heartbeat> findByActive(boolean active);
}
