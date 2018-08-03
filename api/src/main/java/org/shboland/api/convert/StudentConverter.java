package org.shboland.api.convert;

import org.shboland.persistence.db.hibernate.bean.Student;
import org.shboland.domain.entities.JsonStudent;
import org.shboland.api.resource.StudentController;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Service
public class StudentConverter {
    
    public JsonStudent toJson(Student student) {
        JsonStudent jsonStudent = JsonStudent.builder()
                .name(student.getName())
                // @InputJsonField
                .build();
        
        jsonStudent.add(linkTo(StudentController.class).slash(student.getId()).withSelfRel());
        jsonStudent.add(linkTo(StudentController.class).slash(student.getId()).slash("/details").withRel("details"));
            // @InputLink
        
        jsonStudent.add(linkTo(StudentController.class).slash(student.getId()).slash("/results").withRel("results"));

        return jsonStudent;
    }
    
    public Student fromJson(JsonStudent jsonStudent) {
        return studentBuilder(jsonStudent).build();
    }

    public Student fromJson(JsonStudent jsonStudent, long studentId) {
        return studentBuilder(jsonStudent)
                .id(studentId)
                .build();
    }

    private Student.StudentBuilder studentBuilder(JsonStudent jsonStudent) {

        return Student.builder()
                .name(jsonStudent.getName())
                // @InputBeanField
        ;
    }
}