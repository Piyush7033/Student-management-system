package Student.managment.System.repository;

import Student.managment.System.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


    Page<Student> findByFirstNameContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );

    Page<Student> findByDepartmentContainingIgnoreCase(
            String dept,
            Pageable pageable
    );

    Page<Student> findByFirstNameContainingIgnoreCaseAndDepartmentContainingIgnoreCase(
            String keyword,
            String department,
            Pageable pageable
    );


}