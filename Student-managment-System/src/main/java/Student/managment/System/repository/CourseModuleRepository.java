package Student.managment.System.repository;

import Student.managment.System.entity.CourseModule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseModuleRepository extends JpaRepository<CourseModule, Long> {
    Page<CourseModule> findByModuleNameContainingIgnoreCase(String keyword, Pageable pageable);
}