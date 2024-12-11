package todoapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoapplication.domain.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
} 