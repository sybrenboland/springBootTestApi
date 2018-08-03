package org.shboland.core.service;

import java.util.List;
import org.shboland.persistence.criteria.CourseSearchCriteria;
import java.util.Optional;
import org.shboland.persistence.db.hibernate.bean.Course;
import org.shboland.persistence.db.repo.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class CourseService {

    private final CourseRepository courseRepository;
    // @FieldInput

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
        // @ConstructorInput
    }
    
    // @Input
  
    public int findNumberOfCourse(CourseSearchCriteria sc) {
        return courseRepository.findNumberOfCourseBySearchCriteria(sc);
    }
    

    public List<Course> findBySearchCriteria(CourseSearchCriteria sc) {
        return courseRepository.findBySearchCriteria(sc);
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> fetchCourse(long courseId) {
        return courseRepository.findById(courseId);
    }

    public boolean deleteCourse(long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);

        if (course.isPresent()) {
            courseRepository.delete(course.get());
            return true;
        } else {
            return false;
        }
    }
    
}