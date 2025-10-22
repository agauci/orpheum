package com.orpheum.benchmark.airgpt.model;

import com.orpheum.benchmark.model.AbstractEntity;
import lombok.Value;
import lombok.With;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Model class representing the airgpt_conversation table.
 */
@Value
@With
@Table("airgpt_conversation")
public class AirGptConversation extends AbstractEntity<UUID, AirGptConversation> {
    @Id
    UUID id;
    String title;

    // @CreatedDate and @LastModifiedDate do not support OffsetDateTime
    @CreatedDate
    LocalDateTime timestampCreated;

    @PersistenceCreator
    public static AirGptConversation create(UUID id, String title) {
        return new AirGptConversation(id, title, null);
    }

}
