package Student.managment.System.controller;

import Student.managment.System.entity.Student;
import Student.managment.System.service.StudentService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Controller
public class StudentController {

    private final StudentService studentService;

    private static final String UPLOAD_DIR = "uploads/";

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public String listStudents(
            Model model,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String dept,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Student> studentPage =
                studentService.searchStudents(keyword, dept, pageable);

        model.addAttribute("studentPage", studentPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("dept", dept);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute(
                "reverseSortDir",
                sortDir.equals("asc") ? "desc" : "asc"
        );

        return "students";
    }

    @GetMapping("/students/new")
    public String createStudentForm(Model model) {

        model.addAttribute("student", new Student());

        return "create_student";
    }

    @PostMapping("/students")
    public String saveStudent(
            @ModelAttribute Student student,
            @RequestParam("imageFile") MultipartFile imageFile
    ) throws IOException {

        if (!imageFile.isEmpty()) {

            String fileName =
                    System.currentTimeMillis() + "_" +
                            imageFile.getOriginalFilename();

            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(
                    imageFile.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );

            student.setImageName(fileName);
        }

        studentService.saveStudent(student);

        return "redirect:/students";
    }

    @GetMapping("/students/edit/{id}")
    public String editStudentForm(
            @PathVariable Long id,
            Model model
    ) {

        Student student =
                studentService.getStudentById(id);

        model.addAttribute("student", student);

        return "edit_student";
    }

    @PostMapping("/students/{id}")
    public String updateStudent(
            @PathVariable Long id,
            @ModelAttribute Student student,
            @RequestParam(value = "imageFile", required = false)
            MultipartFile imageFile
    ) throws IOException {

        Student existingStudent =
                studentService.getStudentById(id);

        existingStudent.setFirstName(student.getFirstName());
        existingStudent.setLastName(student.getLastName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setDepartment(student.getDepartment());

        if (imageFile != null && !imageFile.isEmpty()) {

            String fileName =
                    System.currentTimeMillis() + "_" +
                            imageFile.getOriginalFilename();

            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Files.copy(
                    imageFile.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );

            existingStudent.setImageName(fileName);
        }

        studentService.updateStudent(existingStudent);

        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {

        studentService.deleteStudentById(id);

        return "redirect:/students";
    }
}