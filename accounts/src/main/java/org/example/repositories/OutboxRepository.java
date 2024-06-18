package org.example.repositories;

import org.example.models.entities.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {
    @Query("SELECT o FROM Outbox o WHERE o.processed = false AND o.aggregateType = 'PayloadNotification'")
    List<Outbox> findUnprocessedPayloadNotification();
}

