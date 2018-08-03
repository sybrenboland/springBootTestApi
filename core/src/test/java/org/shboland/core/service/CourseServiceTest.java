package org.shboland.core.service;

import java.util.Collections;
import java.util.List;
import org.shboland.persistence.criteria.CourseSearchCriteria;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import java.util.Optional;
import org.shboland.persistence.db.hibernate.bean.Course;
import org.shboland.persistence.db.repo.CourseRepository;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CourseServiceTest {
    
    @InjectMocks
    private CourseService courseService;
    
    @Mock
    private CourseRepository courseRepository;

    // @MockInput
        
    private static final int NUMBER_OF_COURSES = 2;
        
    private CourseSearchCriteria courseSearchCriteria = CourseSearchCriteria.builder().build();
        
    private Course course = Course.builder().build();
        
    private static final long COURSE_ID = 3L;
    
    // @Input
    
    @Test
    public void testFindNumberOfCourse() {

        when(courseRepository.findNumberOfCourseBySearchCriteria(courseSearchCriteria)).thenReturn(NUMBER_OF_COURSES);

        int resultNumber = courseService.findNumberOfCourse(courseSearchCriteria);

        assertEquals("Wrong number returned!", NUMBER_OF_COURSES, resultNumber);
    } 
 
    @Test
    public void testFindBySearchCriteria() {

        when(courseRepository.findBySearchCriteria(courseSearchCriteria)).thenReturn(Collections.singletonList(course));

        List<Course> resultCourseList = courseService.findBySearchCriteria(courseSearchCriteria);

        assertNotNull("No object returned!", resultCourseList);
        assertEquals("Wrong number of objects returned!", 1, resultCourseList.size());
        assertEquals("Wrong object returned!", course, resultCourseList.get(0));
    }

    @Test
    public void testSaveCourse() {

        when(courseRepository.save(course)).thenReturn(course);

        Course savedCourse = courseService.save(course);

        assertNotNull("Wrong result returned!", savedCourse);
        assertEquals("Wrong object returned!", course, savedCourse);
    }

    @Test
    public void testFetchCourse() {

        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));

        Optional<Course> fetchResult = courseService.fetchCourse(COURSE_ID);

        assertTrue("Wrong result returned!", fetchResult.isPresent());
        assertEquals("Wrong object returned!", course, fetchResult.get());
    }
    
    @Test
    public void testDeleteCourse() {

        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));

        boolean resultDelete = courseService.deleteCourse(COURSE_ID);

        assertTrue("Wrong result returned!", resultDelete);
        verify(courseRepository, times(1)).delete(course);
    }

    @Test
    public void testDeleteCourse_NoCourseFound() {

        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.empty());

        boolean resultDelete = courseService.deleteCourse(COURSE_ID);

        assertFalse("Wrong result returned!", resultDelete);
        verify(courseRepository, never()).delete(course);
    }
}