package todoapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoapplication.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
} 