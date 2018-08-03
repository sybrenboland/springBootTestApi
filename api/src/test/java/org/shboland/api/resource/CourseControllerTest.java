package org.shboland.api.resource;

import org.shboland.api.convert.ConvertException;
import org.shboland.api.convert.CourseSearchCriteriaConverter;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.persistence.criteria.CourseSearchCriteria;
import org.shboland.domain.entities.JsonCourseSearchCriteria;
import java.util.Collections;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import org.shboland.api.convert.CourseConverter;
import org.shboland.domain.entities.JsonCourse;
import org.shboland.persistence.db.hibernate.bean.Course;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.shboland.core.service.CourseService;
import org.junit.Test;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CourseControllerTest {
    
    @InjectMocks
    private CourseController courseController;
    
    @Mock
    private CourseService courseService;

    @Mock
    private CourseConverter courseConverter;

    @Mock
    private CourseSearchCriteriaConverter courseSearchCriteriaConverter;

    // @MockInput
        
    private CourseSearchCriteria courseSearchCriteria = CourseSearchCriteria.builder().build();
        
    private JsonCourseSearchCriteria jsonCourseSearchCriteria = JsonCourseSearchCriteria.builder().build();
        
    private JsonCourse jsonCourse = JsonCourse.builder().build();
        
    private Course course = Course.builder().build();
        
    private static final long COURSE_ID = 3L;
    
    // @Input 
        
    @Test
    public void testGetCourse_NoCourseFound() {

        when(courseService.fetchCourse(COURSE_ID)).thenReturn(Optional.empty());

        ResponseEntity response = courseController.getCourse(COURSE_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetCourse_WithCourse() {

        when(courseService.fetchCourse(COURSE_ID)).thenReturn(Optional.of(course));
        when(courseConverter.toJson(course)).thenReturn(jsonCourse);

        ResponseEntity response = courseController.getCourse(COURSE_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonCourse);
        assertEquals("Wrong object returned!", jsonCourse, response.getBody());
    }
    
    @Test
    public void testList_ConversionException() {

        when(courseSearchCriteriaConverter.createSearchCriteria(jsonCourseSearchCriteria)).thenThrow(new ConvertException("Conversion fail"));

        ResponseEntity response = courseController.list(jsonCourseSearchCriteria);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        verify(courseService, never()).findNumberOfCourse(any());
    }

    @Test
    public void testList_ZeroCoursesFound() {

        when(courseSearchCriteriaConverter.createSearchCriteria(jsonCourseSearchCriteria)).thenReturn(courseSearchCriteria);
        when(courseService.findNumberOfCourse(courseSearchCriteria)).thenReturn(0);

        ResponseEntity response = courseController.list(jsonCourseSearchCriteria);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonSearchResult);
        JsonSearchResult jsonSearchResult = (JsonSearchResult) response.getBody();
        assertEquals("Wrong object returned!", new Integer(0), jsonSearchResult.getGrandTotalNumberOfResults());
        assertEquals("Wrong object returned!", new Integer(0), jsonSearchResult.getNumberOfResults());
        assertTrue("Wrong object returned!", jsonSearchResult.getResults().isEmpty());
        verify(courseService, never()).findBySearchCriteria(any());
    }

    @Test
    public void testList_MultipleCoursesFound() {

        when(courseSearchCriteriaConverter.createSearchCriteria(jsonCourseSearchCriteria)).thenReturn(courseSearchCriteria);
        when(courseService.findNumberOfCourse(courseSearchCriteria)).thenReturn(1);
        when(courseService.findBySearchCriteria(courseSearchCriteria)).thenReturn(Collections.singletonList(course));
        when(courseConverter.toJson(course)).thenReturn(jsonCourse);

        ResponseEntity response = courseController.list(jsonCourseSearchCriteria);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonSearchResult);
        JsonSearchResult jsonSearchResult = (JsonSearchResult) response.getBody();
        assertEquals("Wrong object returned!", new Integer(1), jsonSearchResult.getGrandTotalNumberOfResults());
        assertEquals("Wrong object returned!", new Integer(1), jsonSearchResult.getNumberOfResults());
        assertEquals("Wrong object returned!", 1, jsonSearchResult.getResults().size());
        assertEquals("Wrong object returned!", jsonCourse, jsonSearchResult.getResults().get(0));
    }
        
    @Test
    public void testPostCourse() {

        when(courseConverter.fromJson(jsonCourse)).thenReturn(course);
        when(courseService.save(course)).thenReturn(course);
        when(courseConverter.toJson(course)).thenReturn(jsonCourse);

        ResponseEntity response = courseController.postCourse(jsonCourse);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonCourse);
        assertEquals("Wrong object returned!", jsonCourse, response.getBody());
    }
    
    @Test
    public void testPutCourse_CreateNewCourse() {

        when(courseService.fetchCourse(COURSE_ID)).thenReturn(Optional.empty());
        when(courseService.save(any())).thenReturn(course);

        ResponseEntity response = courseController.putCourse(COURSE_ID, jsonCourse);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        verify(courseConverter, times(1)).fromJson(jsonCourse);
        verify(courseConverter, never()).fromJson(jsonCourse, COURSE_ID);
        verify(courseConverter, times(1)).toJson(any());
    }

    @Test
    public void testPutCourse_UpdateCourse() {

        when(courseService.fetchCourse(COURSE_ID)).thenReturn(Optional.of(course));
        when(courseService.save(any())).thenReturn(course);

        ResponseEntity response = courseController.putCourse(COURSE_ID, jsonCourse);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        verify(courseConverter, never()).fromJson(jsonCourse);
        verify(courseConverter, times(1)).fromJson(jsonCourse, COURSE_ID);
        verify(courseConverter, times(1)).toJson(any());
    }
    
    @Test
    public void testDeleteCourse() {

        when(courseService.deleteCourse(COURSE_ID)).thenReturn(true);

        ResponseEntity response = courseController.deleteCourse(COURSE_ID);

        assertNotNull("No object returned.", response);
        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testDeleteCourse_NoCourseFound() {

        when(courseService.deleteCourse(COURSE_ID)).thenReturn(false);

        ResponseEntity response = courseController.deleteCourse(COURSE_ID);

        assertNotNull("No object returned.", response);
        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }
}