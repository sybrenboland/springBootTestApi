package org.shboland.api.resource;

import org.shboland.domain.entities.JsonStudent;
import org.shboland.api.convert.StudentConverter;
import org.shboland.core.service.StudentService;
import org.shboland.persistence.db.hibernate.bean.Student;
import org.shboland.api.convert.ConvertException;
import org.shboland.api.convert.ResultSearchCriteriaConverter;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.persistence.criteria.ResultSearchCriteria;
import org.shboland.domain.entities.JsonResultSearchCriteria;
import java.util.Collections;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import org.shboland.api.convert.ResultConverter;
import org.shboland.domain.entities.JsonResult;
import org.shboland.persistence.db.hibernate.bean.Result;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.shboland.core.service.ResultService;
import org.junit.Test;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResultControllerTest {
    
    @InjectMocks
    private ResultController resultController;
    
    @Mock
    private ResultService resultService;

    @Mock
    private ResultConverter resultConverter;

    @Mock
    private ResultSearchCriteriaConverter resultSearchCriteriaConverter;

    @Mock
    private StudentService studentService;

    @Mock
    private StudentConverter studentConverter;

    // @MockInput
        
    private JsonStudent jsonStudent = JsonStudent.builder().build();
        
    private Student student = Student.builder().build();
        
    private static final long STUDENT_ID = 3L;
        
    private ResultSearchCriteria resultSearchCriteria = ResultSearchCriteria.builder().build();
        
    private JsonResultSearchCriteria jsonResultSearchCriteria = JsonResultSearchCriteria.builder().build();
        
    private JsonResult jsonResult = JsonResult.builder().build();
        
    private Result result = Result.builder().build();
        
    private static final long RESULT_ID = 3L;
    
    // @Input
        
    @Test
    public void testGetStudent_NoStudentFound() {

        when(studentService.fetchStudentForResult(RESULT_ID)).thenReturn(Optional.empty());

        ResponseEntity response = resultController.getStudent(RESULT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetStudent_WithStudent() {

        when(studentService.fetchStudentForResult(RESULT_ID)).thenReturn(Optional.of(student));
        when(studentConverter.toJson(student)).thenReturn(jsonStudent);

        ResponseEntity response = resultController.getStudent(RESULT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonStudent);
        assertEquals("Wrong object returned!", jsonStudent, response.getBody());
    }
        
    @Test
    public void testDeleteStudentWithResult_DeleteFailed() {

        when(resultService.removeStudent(RESULT_ID, STUDENT_ID)).thenReturn(false);

        ResponseEntity response = resultController.deleteStudentWithResult(RESULT_ID, STUDENT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testDeleteStudentWithResult_DeleteSucces() {

        when(resultService.removeStudent(RESULT_ID, STUDENT_ID)).thenReturn(true);

        ResponseEntity response = resultController.deleteStudentWithResult(RESULT_ID, STUDENT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
    } 
        
    @Test
    public void testPutStudentWithResult_UpdateFailed() {

        when(resultService.updateResultWithStudent(RESULT_ID, STUDENT_ID)).thenReturn(false);

        ResponseEntity response = resultController.putStudentWithResult(RESULT_ID, STUDENT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testPutStudentWithResult_UpdateSucces() {

        when(resultService.updateResultWithStudent(RESULT_ID, STUDENT_ID)).thenReturn(true);

        ResponseEntity response = resultController.putStudentWithResult(RESULT_ID, STUDENT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
    } 
        
    @Test
    public void testGetResult_NoResultFound() {

        when(resultService.fetchResult(RESULT_ID)).thenReturn(Optional.empty());

        ResponseEntity response = resultController.getResult(RESULT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetResult_WithResult() {

        when(resultService.fetchResult(RESULT_ID)).thenReturn(Optional.of(result));
        when(resultConverter.toJson(result)).thenReturn(jsonResult);

        ResponseEntity response = resultController.getResult(RESULT_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonResult);
        assertEquals("Wrong object returned!", jsonResult, response.getBody());
    }
    
    @Test
    public void testList_ConversionException() {

        when(resultSearchCriteriaConverter.createSearchCriteria(jsonResultSearchCriteria)).thenThrow(new ConvertException("Conversion fail"));

        ResponseEntity response = resultController.list(jsonResultSearchCriteria);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        verify(resultService, never()).findNumberOfResult(any());
    }

    @Test
    public void testList_ZeroResultsFound() {

        when(resultSearchCriteriaConverter.createSearchCriteria(jsonResultSearchCriteria)).thenReturn(resultSearchCriteria);
        when(resultService.findNumberOfResult(resultSearchCriteria)).thenReturn(0);

        ResponseEntity response = resultController.list(jsonResultSearchCriteria);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonSearchResult);
        JsonSearchResult jsonSearchResult = (JsonSearchResult) response.getBody();
        assertEquals("Wrong object returned!", new Integer(0), jsonSearchResult.getGrandTotalNumberOfResults());
        assertEquals("Wrong object returned!", new Integer(0), jsonSearchResult.getNumberOfResults());
        assertTrue("Wrong object returned!", jsonSearchResult.getResults().isEmpty());
        verify(resultService, never()).findBySearchCriteria(any());
    }

    @Test
    public void testList_MultipleResultsFound() {

        when(resultSearchCriteriaConverter.createSearchCriteria(jsonResultSearchCriteria)).thenReturn(resultSearchCriteria);
        when(resultService.findNumberOfResult(resultSearchCriteria)).thenReturn(1);
        when(resultService.findBySearchCriteria(resultSearchCriteria)).thenReturn(Collections.singletonList(result));
        when(resultConverter.toJson(result)).thenReturn(jsonResult);

        ResponseEntity response = resultController.list(jsonResultSearchCriteria);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonSearchResult);
        JsonSearchResult jsonSearchResult = (JsonSearchResult) response.getBody();
        assertEquals("Wrong object returned!", new Integer(1), jsonSearchResult.getGrandTotalNumberOfResults());
        assertEquals("Wrong object returned!", new Integer(1), jsonSearchResult.getNumberOfResults());
        assertEquals("Wrong object returned!", 1, jsonSearchResult.getResults().size());
        assertEquals("Wrong object returned!", jsonResult, jsonSearchResult.getResults().get(0));
    }
        
    @Test
    public void testPostResult() {

        when(resultConverter.fromJson(jsonResult)).thenReturn(result);
        when(resultService.save(result)).thenReturn(result);
        when(resultConverter.toJson(result)).thenReturn(jsonResult);

        ResponseEntity response = resultController.postResult(jsonResult);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonResult);
        assertEquals("Wrong object returned!", jsonResult, response.getBody());
    }
    
    @Test
    public void testPutResult_CreateNewResult() {

        when(resultService.fetchResult(RESULT_ID)).thenReturn(Optional.empty());
        when(resultService.save(any())).thenReturn(result);

        ResponseEntity response = resultController.putResult(RESULT_ID, jsonResult);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        verify(resultConverter, times(1)).fromJson(jsonResult);
        verify(resultConverter, never()).fromJson(jsonResult, RESULT_ID);
        verify(resultConverter, times(1)).toJson(any());
    }

    @Test
    public void testPutResult_UpdateResult() {

        when(resultService.fetchResult(RESULT_ID)).thenReturn(Optional.of(result));
        when(resultService.save(any())).thenReturn(result);

        ResponseEntity response = resultController.putResult(RESULT_ID, jsonResult);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        verify(resultConverter, never()).fromJson(jsonResult);
        verify(resultConverter, times(1)).fromJson(jsonResult, RESULT_ID);
        verify(resultConverter, times(1)).toJson(any());
    }
    
    @Test
    public void testDeleteResult() {

        when(resultService.deleteResult(RESULT_ID)).thenReturn(true);

        ResponseEntity response = resultController.deleteResult(RESULT_ID);

        assertNotNull("No object returned.", response);
        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testDeleteResult_NoResultFound() {

        when(resultService.deleteResult(RESULT_ID)).thenReturn(false);

        ResponseEntity response = resultController.deleteResult(RESULT_ID);

        assertNotNull("No object returned.", response);
        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }
}