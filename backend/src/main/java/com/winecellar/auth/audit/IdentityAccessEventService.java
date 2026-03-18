package com.winecellar.auth.audit;

import com.winecellar.auth.domain.IdentityAccessEvent;
import com.winecellar.auth.domain.IdentityEventType;
import com.winecellar.auth.repo.IdentityAccessEventRepository;
import java.time.Instant;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class IdentityAccessEventService {

    private final IdentityAccessEventRepository repository;

    public IdentityAccessEventService(IdentityAccessEventRepository repository) {
        this.repository = repository;
    }

    public void append(
            String actorUserId,
            String subjectUserAccountId,
            IdentityEventType eventType,
            Map<String, String> metadata) {
        repository.save(new IdentityAccessEvent(
                null,
                actorUserId,
                subjectUserAccountId,
                eventType,
                Instant.now(),
                "USER_ACCOUNT",
                subjectUserAccountId,
                metadata));
    }
}
