package com.bit.app.repository;

import com.bit.app.entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LinkRepository extends JpaRepository<Link, UUID> {
    boolean existsByAlias(String alias);

    boolean existsByShortCode(String shortCode);

    @Query("SELECT l FROM Link l WHERE (l.shortCode = :code OR l.alias = :code) " + "AND (l.expiresAt IS NULL OR l.expiresAt > :now)")
    Optional<Link> findValidLink(String code, Instant now);

    @Modifying
    @Transactional
    @Query("UPDATE Link l SET l.clickCount = l.clickCount + 1 WHERE l.shortCode = :code OR l.alias = :code")
    void incrementClickCount(String code);

    @Modifying
    @Transactional // Required for delete operations
    @Query("DELETE FROM Link l WHERE l.expiresAt IS NOT NULL AND l.expiresAt < :now")
    int deleteByExpiresAtBefore(Instant now);
}