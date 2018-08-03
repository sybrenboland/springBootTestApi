package integration;
         
import org.shboland.persistence.db.repo.CourseRepository;
import org.shboland.persistence.db.hibernate.bean.Course;
import org.shboland.domain.entities.JsonCourse;
import java.util.HashSet;
import org.shboland.persistence.db.repo.ResultRepository;
import org.shboland.persistence.db.hibernate.bean.Result;
import org.shboland.domain.entities.JsonResult;
import org.shboland.persistence.db.repo.DetailsRepository;
import org.shboland.persistence.db.hibernate.bean.Details;
import org.shboland.domain.entities.JsonDetails;
import org.shboland.persistence.db.repo.StudentRepository;
import org.shboland.persistence.db.hibernate.bean.Student;
import org.shboland.domain.entities.JsonStudent;

public class IntegrationTestFactory {

    // @Input

    public static Course givenACourse(CourseRepository courseRepository) {
        return givenACourse(Course.builder()
                 .master(true)
                // @FieldInputCourseBean
                .build(),
                courseRepository);
    }
    
    public static Course givenACourse(Course course, CourseRepository courseRepository) {
        return courseRepository.save(course);
    }
    
    public static JsonCourse givenAJsonCourse() {
        return JsonCourse.builder()
                 .master(false)
                // @FieldInputJsonCourse
                .build();
    }
        

   public static Result givenAResultWithStudent(ResultRepository resultRepository, StudentRepository studentRepository) {
        Student student = studentRepository.save(Student.builder()
            .resultSet(new HashSet<>())
            .build());

        Result result = resultRepository.save(Result.builder()
                .student(student)
                // @FieldInput
                .build());
        
        student.getResultSet().add(result);
        return result;
    }

   public static Student givenAStudentWithResult(StudentRepository studentRepository, ResultRepository resultRepository) {
        Student student = studentRepository.save(Student.builder()
            .resultSet(new HashSet<>())
            .build());

        Result result = resultRepository.save(Result.builder()
                .student(student)
                // @FieldInput
                .build());

        student.getResultSet().add(result);
        return student;
    }

    public static Result givenAResult(ResultRepository resultRepository) {
        return givenAResult(Result.builder()
                 .grade(3)
                // @FieldInputResultBean
                .build(),
                resultRepository);
    }
    
    public static Result givenAResult(Result result, ResultRepository resultRepository) {
        return resultRepository.save(result);
    }
    
    public static JsonResult givenAJsonResult() {
        return JsonResult.builder()
                 .grade(4)
                // @FieldInputJsonResult
                .build();
    }
        

    public static Details givenADetails(DetailsRepository detailsRepository) {
        return givenADetails(Details.builder()
                 .number(3147483647L)
                 .student(Student.builder().build())
                // @FieldInputDetailsBean
                .build(),
                detailsRepository);
    }
    
    public static Details givenADetails(Details details, DetailsRepository detailsRepository) {
        return detailsRepository.save(details);
    }
    
    public static JsonDetails givenAJsonDetails() {
        return JsonDetails.builder()
                 .number(4447483647L)
                // @FieldInputJsonDetails
                .build();
    }
        

    public static Student givenAStudent(StudentRepository studentRepository) {
        return givenAStudent(Student.builder()
                 .name("string")
                // @FieldInputStudentBean
                .build(),
                studentRepository);
    }
    
    public static Student givenAStudent(Student student, StudentRepository studentRepository) {
        return studentRepository.save(student);
    }
    
    public static JsonStudent givenAJsonStudent() {
        return JsonStudent.builder()
                 .name("different string")
                // @FieldInputJsonStudent
                .build();
    }
        
}
