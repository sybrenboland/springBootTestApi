package org.shboland.api.resource;

import org.shboland.persistence.db.hibernate.bean.Course;
import java.util.stream.Collectors;
import javax.ws.rs.BeanParam;
import java.util.List;
import java.util.ArrayList;
import org.shboland.persistence.criteria.CourseSearchCriteria;
import org.shboland.api.convert.ConvertException;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.domain.entities.JsonCourseSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.shboland.api.convert.CourseSearchCriteriaConverter;
import org.springframework.http.HttpStatus;
import org.shboland.domain.entities.JsonCourse;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.shboland.core.service.CourseService;
import org.shboland.api.convert.CourseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CourseController implements ICourseController {

    private final CourseService courseService;
    private final CourseConverter courseConverter;
    private final CourseSearchCriteriaConverter courseSearchCriteriaConverter;
    // @FieldInput

    @Autowired
    public CourseController(CourseSearchCriteriaConverter courseSearchCriteriaConverter, CourseService courseService, CourseConverter courseConverter) {
        this.courseService = courseService;
        this.courseConverter = courseConverter;
        this.courseSearchCriteriaConverter = courseSearchCriteriaConverter;
        // @ConstructorInput
    }
    
    // @Input

    @Override
    public ResponseEntity<JsonCourse> getCourse(@PathVariable long courseId) {
        Optional<Course> courseOptional = courseService.fetchCourse(courseId);

        return courseOptional.isPresent() ?
                ResponseEntity.ok(courseConverter.toJson(courseOptional.get())) :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<JsonSearchResult> list(@BeanParam JsonCourseSearchCriteria searchCriteria) {

        CourseSearchCriteria sc;
        try {
            sc = courseSearchCriteriaConverter.createSearchCriteria(searchCriteria);
        } catch (ConvertException e) {
            log.warn("Conversion failed!", e);
            return ResponseEntity.badRequest().build();
        }

        List<Course> courseList = new ArrayList<>();
        int numberOfCourse = courseService.findNumberOfCourse(sc);
        if (numberOfCourse > 0) {
            courseList = courseService.findBySearchCriteria(sc);
        }

        JsonSearchResult<JsonCourse> result = JsonSearchResult.<JsonCourse>builder()
                .results(courseList.stream().map(courseConverter::toJson).collect(Collectors.toList()))
                .numberOfResults(courseList.size())
                .grandTotalNumberOfResults(numberOfCourse)
                .build();

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity postCourse(@RequestBody JsonCourse jsonCourse) {
            
        Course newCourse = courseService.save(courseConverter.fromJson(jsonCourse));

        return ResponseEntity.status(HttpStatus.CREATED).body(courseConverter.toJson(newCourse));
    }

    @Override
    public ResponseEntity<JsonCourse> putCourse(@PathVariable long courseId, @RequestBody JsonCourse jsonCourse) {

        Optional<Course> courseOptional = courseService.fetchCourse(courseId);

        Course savedCourse;
        if (!courseOptional.isPresent()) {
            savedCourse = courseService.save(courseConverter.fromJson(jsonCourse));
        } else {
            savedCourse = courseService.save(courseConverter.fromJson(jsonCourse, courseId));
        }

        return ResponseEntity.ok(courseConverter.toJson(savedCourse));
    }

    @Override
    public ResponseEntity deleteCourse(@PathVariable long courseId) {

        return courseService.deleteCourse(courseId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
    
}