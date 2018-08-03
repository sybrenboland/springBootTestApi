package org.shboland.api.resource;

import static org.mockito.ArgumentMatchers.any;
import org.shboland.api.convert.ResultConverter;
import org.shboland.core.service.ResultService;
import org.shboland.persistence.db.hibernate.bean.Result;
import org.shboland.domain.entities.JsonResult;
import java.util.Arrays;
import java.util.ArrayList;
import org.shboland.domain.entities.JsonDetails;
import org.shboland.api.convert.DetailsConverter;
import org.shboland.core.service.DetailsService;
import org.shboland.persistence.db.hibernate.bean.Details;
import org.shboland.api.convert.ConvertException;
import org.shboland.api.convert.StudentSearchCriteriaConverter;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.persistence.criteria.StudentSearchCriteria;
import org.shboland.domain.entities.JsonStudentSearchCriteria;
import java.util.Collections;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import org.shboland.api.convert.StudentConverter;
import org.shboland.domain.entities.JsonStudent;
import org.shboland.persistence.db.hibernate.bean.Student;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.shboland.core.service.StudentService;
import org.junit.Test;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StudentControllerTest {
    
    @InjectMocks
    private StudentController studentController;
    
    @Mock
    private StudentService studentService;

    @Mock
    private StudentConverter studentConverter;

    @Mock
    private StudentSearchCriteriaConverter studentSearchCriteriaConverter;

    @Mock
    private DetailsService detailsService;

    @Mock
    private DetailsConverter detailsConverter;

    @Mock
    private ResultService resultService;

    @Mock
    private ResultConverter resultConverter;

    // @MockInput
        
    private static final long RESULT_ID = 3L;
        
    private static final long DETAILS_ID = 3L;
        
    private JsonDetails jsonDetails = JsonDetails.builder().build();
        
    private Details details = Details.builder().build();
        
    private StudentSearchCriteria studentSearchCriteria = StudentSearchCriteria.builder().build();
        
    private JsonStudentSearchCriteria jsonStudentSearchCriteria = JsonStudentSearchCriteria.builder().build();
        
    private JsonStudent jsonStudent = JsonStudent.builder().build();
        
    private Student student = Student.builder().build();
        
    private static final long STUDENT_ID = 3L;
    
    // @Input
        
    @Test
    public void testGetResults_NoResultFound() {

        when(resultService.fetchResultsForStudent(STUDENT_ID)).thenReturn(new ArrayList<>());

        ResponseEntity response = studentController.getResults(STUDENT_ID);


        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonSearchResult);
        JsonSearchResult jsonSearchResult = (JsonSearchResult) response.getBody();
        assertEquals("Wrong object returned!", new Integer(0), jsonSearchResult.getGrandTotalNumberOfResults());
        assertEquals("Wrong object returned!", new Integer(0), jsonSearchResult.getNumberOfResults());
        assertTrue("Wrong object returned!", jsonSearchResult.getResults().isEmpty());
    }

    @Test
    public void testGetResults_MultipleResultsFound() {

        when(resultService.fetchResultsForStudent(STUDENT_ID)).thenReturn(Arrays.asList(
                Result.builder().build(),
                Result.builder().build()));
        when(resultConverter.toJson(any()))
                .thenReturn(JsonResult.builder().build())
                .thenReturn(JsonResult.builder().build());

        ResponseEntity response = studentController.getResults(STUDENT_ID);


        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonSearchResult);
        JsonSearchResult jsonSearchResult = (JsonSearchResult) response.getBody();
        assertEquals("Wrong object returned!", new Integer(2), jsonSearchResult.getGrandTotalNumberOfResults());
        assertEquals("Wrong object returned!", new Integer(2), jsonSearchResult.getNumberOfResults());
        assertEquals("Wrong number of objects returned!", 2, jsonSearchResult.getResults().size());
    }
        
    @Test
    public void testDeleteResultWithStudent_DeleteFailed() {

        when(studentService.removeResult(STUDENT_ID, RESULT_ID)).thenReturn(false);

        ResponseEntity response = studentController.deleteResultWithStudent(STUDENT_ID, RESULT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testDeleteResultWithStudent_DeleteSucces() {

        when(studentService.removeResult(STUDENT_ID, RESULT_ID)).thenReturn(true);

        ResponseEntity response = studentController.deleteResultWithStudent(STUDENT_ID, RESULT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
    } 
        
    @Test
    public void testPutResultWithStudent_UpdateFailed() {

        when(studentService.updateStudentWithResult(STUDENT_ID, RESULT_ID)).thenReturn(false);

        ResponseEntity response = studentController.putResultWithStudent(STUDENT_ID, RESULT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testPutResultWithStudent_UpdateSucces() {

        when(studentService.updateStudentWithResult(STUDENT_ID, RESULT_ID)).thenReturn(true);

        ResponseEntity response = studentController.putResultWithStudent(STUDENT_ID, RESULT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
    } 
        
   @Test
    public void testDeleteDetailsWithStudent_NoDetailsFound() {

        when(detailsService.deleteDetails(STUDENT_ID)).thenReturn(false);

        ResponseEntity response = studentController.deleteDetailsWithStudent(STUDENT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testDeleteDetailsWithStudent() {

        when(detailsService.deleteDetails(STUDENT_ID)).thenReturn(true);

        ResponseEntity response = studentController.deleteDetailsWithStudent(STUDENT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
    } 
        
    @Test
    public void testPutDetailsWithStudent_UpdateFailed() {

        when(studentService.updateStudentWithDetails(STUDENT_ID, DETAILS_ID)).thenReturn(false);

        ResponseEntity response = studentController.putDetailsWithStudent(STUDENT_ID, DETAILS_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testPutDetailsWithStudent_UpdateSucces() {

        when(studentService.updateStudentWithDetails(STUDENT_ID, DETAILS_ID)).thenReturn(true);

        ResponseEntity response = studentController.putDetailsWithStudent(STUDENT_ID, DETAILS_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetDetails_NoDetailsFound() {

        when(detailsService.fetchDetails(STUDENT_ID)).thenReturn(Optional.empty());

        ResponseEntity response = studentController.getDetails(STUDENT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetDetails() {

        when(detailsService.fetchDetails(STUDENT_ID)).thenReturn(Optional.of(details));
        when(detailsConverter.toJson(details)).thenReturn(jsonDetails);

        ResponseEntity response = studentController.getDetails(STUDENT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonDetails);
        assertEquals("Wrong object returned!", jsonDetails, response.getBody());
    } 
        
    @Test
    public void testGetStudent_NoStudentFound() {

        when(studentService.fetchStudent(STUDENT_ID)).thenReturn(Optional.empty());

        ResponseEntity response = studentController.getStudent(STUDENT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetStudent_WithStudent() {

        when(studentService.fetchStudent(STUDENT_ID)).thenReturn(Optional.of(student));
        when(studentConverter.toJson(student)).thenReturn(jsonStudent);

        ResponseEntity response = studentController.getStudent(STUDENT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonStudent);
        assertEquals("Wrong object returned!", jsonStudent, response.getBody());
    }
    
    @Test
    public void testList_ConversionException() {

        when(studentSearchCriteriaConverter.createSearchCriteria(jsonStudentSearchCriteria)).thenThrow(new ConvertException("Conversion fail"));

        ResponseEntity response = studentController.list(jsonStudentSearchCriteria);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        verify(studentService, never()).findNumberOfStudent(any());
    }

    @Test
    public void testList_ZeroStudentsFound() {

        when(studentSearchCriteriaConverter.createSearchCriteria(jsonStudentSearchCriteria)).thenReturn(studentSearchCriteria);
        when(studentService.findNumberOfStudent(studentSearchCriteria)).thenReturn(0);

        ResponseEntity response = studentController.list(jsonStudentSearchCriteria);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonSearchResult);
        JsonSearchResult jsonSearchResult = (JsonSearchResult) response.getBody();
        assertEquals("Wrong object returned!", new Integer(0), jsonSearchResult.getGrandTotalNumberOfResults());
        assertEquals("Wrong object returned!", new Integer(0), jsonSearchResult.getNumberOfResults());
        assertTrue("Wrong object returned!", jsonSearchResult.getResults().isEmpty());
        verify(studentService, never()).findBySearchCriteria(any());
    }

    @Test
    public void testList_MultipleStudentsFound() {

        when(studentSearchCriteriaConverter.createSearchCriteria(jsonStudentSearchCriteria)).thenReturn(studentSearchCriteria);
        when(studentService.findNumberOfStudent(studentSearchCriteria)).thenReturn(1);
        when(studentService.findBySearchCriteria(studentSearchCriteria)).thenReturn(Collections.singletonList(student));
        when(studentConverter.toJson(student)).thenReturn(jsonStudent);

        ResponseEntity response = studentController.list(jsonStudentSearchCriteria);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonSearchResult);
        JsonSearchResult jsonSearchResult = (JsonSearchResult) response.getBody();
        assertEquals("Wrong object returned!", new Integer(1), jsonSearchResult.getGrandTotalNumberOfResults());
        assertEquals("Wrong object returned!", new Integer(1), jsonSearchResult.getNumberOfResults());
        assertEquals("Wrong object returned!", 1, jsonSearchResult.getResults().size());
        assertEquals("Wrong object returned!", jsonStudent, jsonSearchResult.getResults().get(0));
    }
        
    @Test
    public void testPostStudent() {

        when(studentConverter.fromJson(jsonStudent)).thenReturn(student);
        when(studentService.save(student)).thenReturn(student);
        when(studentConverter.toJson(student)).thenReturn(jsonStudent);

        ResponseEntity response = studentController.postStudent(jsonStudent);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonStudent);
        assertEquals("Wrong object returned!", jsonStudent, response.getBody());
    }
    
    @Test
    public void testPutStudent_CreateNewStudent() {

        when(studentService.fetchStudent(STUDENT_ID)).thenReturn(Optional.empty());
        when(studentService.save(any())).thenReturn(student);

        ResponseEntity response = studentController.putStudent(STUDENT_ID, jsonStudent);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        verify(studentConverter, times(1)).fromJson(jsonStudent);
        verify(studentConverter, never()).fromJson(jsonStudent, STUDENT_ID);
        verify(studentConverter, times(1)).toJson(any());
    }

    @Test
    public void testPutStudent_UpdateStudent() {

        when(studentService.fetchStudent(STUDENT_ID)).thenReturn(Optional.of(student));
        when(studentService.save(any())).thenReturn(student);

        ResponseEntity response = studentController.putStudent(STUDENT_ID, jsonStudent);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        verify(studentConverter, never()).fromJson(jsonStudent);
        verify(studentConverter, times(1)).fromJson(jsonStudent, STUDENT_ID);
        verify(studentConverter, times(1)).toJson(any());
    }
    
    @Test
    public void testDeleteStudent() {

        when(studentService.deleteStudent(STUDENT_ID)).thenReturn(true);

        ResponseEntity response = studentController.deleteStudent(STUDENT_ID);

        assertNotNull("No object returned.", response);
        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testDeleteStudent_NoStudentFound() {

        when(studentService.deleteStudent(STUDENT_ID)).thenReturn(false);

        ResponseEntity response = studentController.deleteStudent(STUDENT_ID);

        assertNotNull("No object returned.", response);
        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }
}