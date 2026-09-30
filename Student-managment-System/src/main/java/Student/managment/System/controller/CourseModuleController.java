package Student.managment.System.controller;

import Student.managment.System.entity.CourseModule;
import Student.managment.System.service.CourseModuleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/course")
public class CourseModuleController {

    private final CourseModuleService service;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public CourseModuleController(CourseModuleService service) {
        this.service = service;
    }

    @GetMapping
    public String home(
            Model model,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CourseModule> modulePage =
                service.getModules(keyword, pageable);

        model.addAttribute("modulePage", modulePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", modulePage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        return "course_module";
    }


    @GetMapping("/add")
    public String addModuleForm(Model model) {

        model.addAttribute("module", new CourseModule());

        return "add-module";
    }


    @GetMapping("/edit/{id}")
    public String editModule(
            @PathVariable Long id,
            Model model) {

        CourseModule module =
                service.getModuleById(id);

        if (module == null) {
            return "redirect:/course";
        }

        model.addAttribute("module", module);

        return "add-module";
    }

    @PostMapping("/save")
    public String saveModule(
            @ModelAttribute CourseModule module,
            @RequestParam(value = "pdfFile", required = false)
            MultipartFile pdfFile) {

        try {

            CourseModule existingModule = null;

            if (module.getId() != null) {
                existingModule =
                        service.getModuleById(module.getId());
            }

            if (pdfFile != null && !pdfFile.isEmpty()) {

                // PDF validation
                if (!"application/pdf"
                        .equalsIgnoreCase(pdfFile.getContentType())) {

                    return "redirect:/course/add";
                }

                String fileName =
                        System.currentTimeMillis()
                                + "_"
                                + pdfFile.getOriginalFilename();

                Path uploadPath =
                        Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(
                        pdfFile.getInputStream(),
                        uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING
                );

                module.setPdfFileName(fileName);
                module.setPdfPath(
                        uploadPath.resolve(fileName).toString()
                );

            } else if (existingModule != null) {

                // Keep old PDF if new PDF not uploaded
                module.setPdfFileName(
                        existingModule.getPdfFileName()
                );

                module.setPdfPath(
                        existingModule.getPdfPath()
                );
            }

            service.saveModule(module);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/course";
    }


    @GetMapping("/pdf/{id}")
    public ResponseEntity<Resource> viewPdf(
            @PathVariable Long id) throws Exception {

        CourseModule module =
                service.getModuleById(id);

        if (module == null ||
                module.getPdfPath() == null ||
                module.getPdfPath().isBlank()) {

            return ResponseEntity.notFound().build();
        }

        Path filePath =
                Paths.get(module.getPdfPath());

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource =
                new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +
                                module.getPdfFileName() + "\""
                )
                .body(resource);
    }

    @GetMapping("/delete/{id}")
    public String deleteModule(
            @PathVariable Long id) {

        CourseModule module =
                service.getModuleById(id);

        if (module != null &&
                module.getPdfPath() != null) {

            try {
                Files.deleteIfExists(
                        Paths.get(module.getPdfPath())
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        service.deleteModule(id);

        return "redirect:/course";
    }
}