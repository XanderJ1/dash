package com.bash.dash.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class VerificationCode {
    @Id
    @GeneratedValue
    private Long id;
    private String code;
    private String email;
    private Date expiry;
    private boolean isExpired;

}
