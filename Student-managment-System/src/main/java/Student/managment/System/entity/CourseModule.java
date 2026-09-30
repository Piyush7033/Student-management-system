//package Student.managment.System.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@Entity
//@Table(name = "course_modules")
//public class CourseModule {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String moduleName;
//    private String description;
//    private int duration;
//
//    public CourseModule() {
//    }
//
//    public CourseModule(String moduleName, String description, int duration) {
//        this.moduleName = moduleName;
//        this.description = description;
//        this.duration = duration;
//    }
//
//
//}





package Student.managment.System.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course_modules")
public class CourseModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String moduleName;
    private String description;
    private int duration;


    private String pdfFileName;

    private String pdfPath;

    public CourseModule() {
    }

    public CourseModule(String moduleName, String description, int duration) {
        this.moduleName = moduleName;
        this.description = description;
    }
}