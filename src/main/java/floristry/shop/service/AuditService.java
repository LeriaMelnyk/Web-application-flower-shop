package floristry.shop.service;

import floristry.shop.model.AuditLog;
import floristry.shop.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository repository;

    public void log(String action, String resourceId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null) ? auth.getName() : "anonymous";

        repository.save(AuditLog.builder()
                .username(username)
                .action(action)
                .resource("Flower")
                .resourceId(resourceId)
                .timestamp(Instant.now())
                .build());
    }

    public List<AuditLog> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "timestamp"));
    }
}