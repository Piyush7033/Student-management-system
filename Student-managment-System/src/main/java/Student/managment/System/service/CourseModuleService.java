package Student.managment.System.service;


import Student.managment.System.entity.CourseModule;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;

public interface CourseModuleService {

    List<CourseModule> getAllModules();

    CourseModule saveModule(CourseModule module);

    CourseModule getModuleById(Long id);

    void deleteModule(Long id);

    Page<CourseModule> getModules(String keyword, Pageable pageable);

    Page<CourseModule> getModules(String keyword, org.springframework.data.domain.Pageable pageable);
}