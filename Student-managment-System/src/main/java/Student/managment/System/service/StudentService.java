package Student.managment.System.service;

import Student.managment.System.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {

    List<Student> getAllStudents();

    Student saveStudent(Student student);

    Student getStudentById(Long id);

    Student updateStudent(Student student);

    void deleteStudentById(Long id);

    Page<Student> searchStudents(
            String keyword,
            String dept,
            Pageable pageable
    );

//    Page<Student> getStudents(String keyword, String dept, Pageable pageable);
}