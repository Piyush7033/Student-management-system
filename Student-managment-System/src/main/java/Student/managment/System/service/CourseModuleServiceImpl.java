package Student.managment.System.service;

import Student.managment.System.entity.CourseModule;
import Student.managment.System.repository.CourseModuleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CourseModuleServiceImpl implements CourseModuleService {

    private final CourseModuleRepository repository;

    public CourseModuleServiceImpl(CourseModuleRepository repository) {
        this.repository = repository;
    }


    @Override
    public java.util.List<CourseModule> getAllModules() {
        return repository.findAll();
    }

    @Override
    public CourseModule saveModule(CourseModule module) {
        return repository.save(module);
    }

    @Override
    public CourseModule getModuleById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void deleteModule(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Page<CourseModule> getModules(String keyword, java.awt.print.Pageable pageable) {
        return null;
    }

    @Override
    public Page<CourseModule> getModules(String keyword, Pageable pageable) {

        if (keyword != null && !keyword.isEmpty()) {
            return repository.findByModuleNameContainingIgnoreCase(keyword, (Pageable) pageable);
        }

        return repository.findAll(pageable);
    }
}