package com.bash.dash.users.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Document{
    private String cardId;
    private String cardImageUrl;
    private String licenseId;
    private String userId;
}
