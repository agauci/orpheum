package com.orpheum.orchestrator.backstage.portal.model.user;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("captive_portal_user_data")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class UserData {
    @Id
    Long    id;
    @NonNull String  firstName;
    @NonNull String  lastName;
    @NonNull String  email;
    @NonNull String  consentText;
    @NonNull String  location;
}
