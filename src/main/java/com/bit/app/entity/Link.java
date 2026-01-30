package com.bit.app.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "links", indexes = {
        @Index(name = "idx_short_code", columnList = "short_code"),
        @Index(name = "idx_expires_at", columnList = "expires_at") // Critical for cleanup speed
})
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "link_id", length = 36, updatable = false, nullable = false)
    @JdbcTypeCode(SqlTypes.VARCHAR) // Fixes "Incorrect string value" error
    private UUID linkId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "short_code", unique = true, nullable = false)
    private String shortCode;

    @Column(name = "alias", unique = true)
    private String alias;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "click_count", nullable = false)
    private Long clickCount = 0L;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
}