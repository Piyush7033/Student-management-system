package Student.managment.System.service;

import Student.managment.System.entity.Student;
import Student.managment.System.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @Override
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public Page<Student> searchStudents(
            String keyword,
            String dept,
            Pageable pageable) {

        if (keyword != null && !keyword.isBlank()
                && dept != null && !dept.isBlank()) {

            return studentRepository
                    .findByFirstNameContainingIgnoreCaseAndDepartmentContainingIgnoreCase(
                            keyword,
                            dept,
                            pageable);
        }

        if (keyword != null && !keyword.isBlank()) {
            return studentRepository
                    .findByFirstNameContainingIgnoreCase(
                            keyword,
                            pageable);
        }

        if (dept != null && !dept.isBlank()) {
            return studentRepository
                    .findByDepartmentContainingIgnoreCase(
                            dept,
                            pageable);
        }

        return studentRepository.findAll(pageable);
    }
}