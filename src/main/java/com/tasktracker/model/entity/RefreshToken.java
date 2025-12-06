package com.tasktracker.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = 0;

    @Column(nullable = false, length = 500, name = "`value`")
    private String value;

    @Column(nullable = false, length = 30, name = "`owner_username`")
    private String ownerUsername;

    public RefreshToken(String value, String ownerUsername) {
        this.value = value;
        this.ownerUsername = ownerUsername;
    }
}
