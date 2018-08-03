package org.shboland.api.convert;

import org.shboland.persistence.db.hibernate.bean.Course;
import org.shboland.domain.entities.JsonCourse;
import org.shboland.api.resource.CourseController;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Service
public class CourseConverter {
    
    public JsonCourse toJson(Course course) {
        JsonCourse jsonCourse = JsonCourse.builder()
                .master(course.getMaster())
                // @InputJsonField
                .build();
        
        jsonCourse.add(linkTo(CourseController.class).slash(course.getId()).withSelfRel());
        // @InputLink

        return jsonCourse;
    }
    
    public Course fromJson(JsonCourse jsonCourse) {
        return courseBuilder(jsonCourse).build();
    }

    public Course fromJson(JsonCourse jsonCourse, long courseId) {
        return courseBuilder(jsonCourse)
                .id(courseId)
                .build();
    }

    private Course.CourseBuilder courseBuilder(JsonCourse jsonCourse) {

        return Course.builder()
                .master(jsonCourse.getMaster())
                // @InputBeanField
        ;
    }
}