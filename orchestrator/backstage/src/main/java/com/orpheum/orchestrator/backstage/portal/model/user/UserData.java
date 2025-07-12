package com.orpheum.orchestrator.backstage.portal.model.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("captive_portal_user_data")
@Data
public class UserData {
    @Id
    Long    id;
    String  firstName;
    String  lastName;
    String  email;
    String  consentText;
    String  location;
}
