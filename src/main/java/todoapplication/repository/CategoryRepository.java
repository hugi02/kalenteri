package todoapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import todoapplication.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
} 