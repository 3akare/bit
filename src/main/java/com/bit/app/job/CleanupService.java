package com.bit.app.job;

import com.bit.app.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class CleanupService {
    private final LinkRepository linkRepository;

    @Scheduled(fixedDelay = 3600000)
    public void deletedExpiredLinks(){
        log.info("====== Starting expired link cleanup =====");
        int deleted = linkRepository.deleteByExpiresAtBefore(Instant.now());
        if (deleted > 0) {
            log.info("====== Finished. Deleted {} expired links ======", deleted);
        }
    }
}