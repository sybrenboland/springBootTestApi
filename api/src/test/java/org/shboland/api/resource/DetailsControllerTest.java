package org.shboland.api.resource;

import org.shboland.domain.entities.JsonStudent;
import org.shboland.api.convert.StudentConverter;
import org.shboland.core.service.StudentService;
import org.shboland.persistence.db.hibernate.bean.Student;
import org.shboland.api.convert.ConvertException;
import org.shboland.api.convert.DetailsSearchCriteriaConverter;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.persistence.criteria.DetailsSearchCriteria;
import org.shboland.domain.entities.JsonDetailsSearchCriteria;
import java.util.Collections;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import org.shboland.api.convert.DetailsConverter;
import org.shboland.domain.entities.JsonDetails;
import org.shboland.persistence.db.hibernate.bean.Details;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.shboland.core.service.DetailsService;
import org.junit.Test;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DetailsControllerTest {
    
    @InjectMocks
    private DetailsController detailsController;
    
    @Mock
    private DetailsService detailsService;

    @Mock
    private DetailsConverter detailsConverter;

    @Mock
    private DetailsSearchCriteriaConverter detailsSearchCriteriaConverter;

    @Mock
    private StudentService studentService;

    @Mock
    private StudentConverter studentConverter;

    // @MockInput
        
    private JsonStudent jsonStudent = JsonStudent.builder().build();
        
    private Student student = Student.builder().build();
        
    private DetailsSearchCriteria detailsSearchCriteria = DetailsSearchCriteria.builder().build();
        
    private JsonDetailsSearchCriteria jsonDetailsSearchCriteria = JsonDetailsSearchCriteria.builder().build();
        
    private JsonDetails jsonDetails = JsonDetails.builder().build();
        
    private Details details = Details.builder().build();
        
    private static final long DETAILS_ID = 3L;
    
    // @Input 
        
    @Test
    public void testPutStudentWithDetails_NoStudentFound() {

        when(studentConverter.fromJson(jsonStudent)).thenReturn(student);
        when(detailsService.updateDetailsWithStudent(DETAILS_ID, student)).thenReturn(null);

        ResponseEntity response = detailsController.putStudentWithDetails(DETAILS_ID, jsonStudent);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testPutStudentWithDetails() {

        when(studentConverter.fromJson(jsonStudent)).thenReturn(student);
        when(detailsService.updateDetailsWithStudent(DETAILS_ID, student)).thenReturn(student);
        when(studentConverter.toJson(student)).thenReturn(jsonStudent);

        ResponseEntity response = detailsController.putStudentWithDetails(DETAILS_ID, jsonStudent);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonStudent);
        assertEquals("Wrong object returned!", jsonStudent, response.getBody());
    }

    @Test
    public void testGetStudent_NoStudentFound() {

        when(studentService.fetchStudent(DETAILS_ID)).thenReturn(Optional.empty());

        ResponseEntity response = detailsController.getStudent(DETAILS_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetStudent() {

        when(studentService.fetchStudent(DETAILS_ID)).thenReturn(Optional.of(student));
        when(studentConverter.toJson(student)).thenReturn(jsonStudent);

        ResponseEntity response = detailsController.getStudent(DETAILS_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonStudent);
        assertEquals("Wrong object returned!", jsonStudent, response.getBody());
    } 
        
    @Test
    public void testGetDetails_NoDetailsFound() {

        when(detailsService.fetchDetails(DETAILS_ID)).thenReturn(Optional.empty());

        ResponseEntity response = detailsController.getDetails(DETAILS_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }

    @Test
    public void testGetDetails_WithDetails() {

        when(detailsService.fetchDetails(DETAILS_ID)).thenReturn(Optional.of(details));
        when(detailsConverter.toJson(details)).thenReturn(jsonDetails);

        ResponseEntity response = detailsController.getDetails(DETAILS_ID);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonDetails);
        assertEquals("Wrong object returned!", jsonDetails, response.getBody());
    }
    
    @Test
    public void testList_ConversionException() {

        when(detailsSearchCriteriaConverter.createSearchCriteria(jsonDetailsSearchCriteria)).thenThrow(new ConvertException("Conversion fail"));

        ResponseEntity response = detailsController.list(jsonDetailsSearchCriteria);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        verify(detailsService, never()).findNumberOfDetails(any());
    }

    @Test
    public void testList_ZeroDetailssFound() {

        when(detailsSearchCriteriaConverter.createSearchCriteria(jsonDetailsSearchCriteria)).thenReturn(detailsSearchCriteria);
        when(detailsService.findNumberOfDetails(detailsSearchCriteria)).thenReturn(0);

        ResponseEntity response = detailsController.list(jsonDetailsSearchCriteria);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonSearchResult);
        JsonSearchResult jsonSearchResult = (JsonSearchResult) response.getBody();
        assertEquals("Wrong object returned!", new Integer(0), jsonSearchResult.getGrandTotalNumberOfResults());
        assertEquals("Wrong object returned!", new Integer(0), jsonSearchResult.getNumberOfResults());
        assertTrue("Wrong object returned!", jsonSearchResult.getResults().isEmpty());
        verify(detailsService, never()).findBySearchCriteria(any());
    }

    @Test
    public void testList_MultipleDetailssFound() {

        when(detailsSearchCriteriaConverter.createSearchCriteria(jsonDetailsSearchCriteria)).thenReturn(detailsSearchCriteria);
        when(detailsService.findNumberOfDetails(detailsSearchCriteria)).thenReturn(1);
        when(detailsService.findBySearchCriteria(detailsSearchCriteria)).thenReturn(Collections.singletonList(details));
        when(detailsConverter.toJson(details)).thenReturn(jsonDetails);

        ResponseEntity response = detailsController.list(jsonDetailsSearchCriteria);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonSearchResult);
        JsonSearchResult jsonSearchResult = (JsonSearchResult) response.getBody();
        assertEquals("Wrong object returned!", new Integer(1), jsonSearchResult.getGrandTotalNumberOfResults());
        assertEquals("Wrong object returned!", new Integer(1), jsonSearchResult.getNumberOfResults());
        assertEquals("Wrong object returned!", 1, jsonSearchResult.getResults().size());
        assertEquals("Wrong object returned!", jsonDetails, jsonSearchResult.getResults().get(0));
    }
        
    @Test
    public void testPostDetails() {

        when(detailsConverter.fromJson(jsonDetails)).thenReturn(details);
        when(detailsService.save(details)).thenReturn(details);
        when(detailsConverter.toJson(details)).thenReturn(jsonDetails);

        ResponseEntity response = detailsController.postDetails(jsonDetails);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.CREATED.value(), response.getStatusCodeValue());
        assertTrue("Returned object of wrong type!", response.getBody() instanceof JsonDetails);
        assertEquals("Wrong object returned!", jsonDetails, response.getBody());
    }
    
    @Test
    public void testPutDetails_CreateNewDetails() {

        when(detailsService.fetchDetails(DETAILS_ID)).thenReturn(Optional.empty());

        ResponseEntity response = detailsController.putDetails(DETAILS_ID, jsonDetails);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.BAD_REQUEST.value(), response.getStatusCodeValue());
        verify(detailsConverter, never()).fromJson(jsonDetails);
        verify(detailsConverter, never()).fromJson(jsonDetails, DETAILS_ID);
        verify(detailsConverter, never()).toJson(any());
    }

    @Test
    public void testPutDetails_UpdateDetails() {

        when(detailsService.fetchDetails(DETAILS_ID)).thenReturn(Optional.of(details));
        when(detailsService.save(any())).thenReturn(details);

        ResponseEntity response = detailsController.putDetails(DETAILS_ID, jsonDetails);

        assertNotNull("No response!", response);
        assertEquals("Wrong status code returned!", HttpStatus.OK.value(), response.getStatusCodeValue());
        verify(detailsConverter, never()).fromJson(jsonDetails);
        verify(detailsConverter, times(1)).fromJson(jsonDetails, DETAILS_ID);
        verify(detailsConverter, times(1)).toJson(any());
    }
    
    @Test
    public void testDeleteDetails() {

        when(detailsService.deleteDetails(DETAILS_ID)).thenReturn(true);

        ResponseEntity response = detailsController.deleteDetails(DETAILS_ID);

        assertNotNull("No object returned.", response);
        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatusCodeValue());
    }

    @Test
    public void testDeleteDetails_NoDetailsFound() {

        when(detailsService.deleteDetails(DETAILS_ID)).thenReturn(false);

        ResponseEntity response = detailsController.deleteDetails(DETAILS_ID);

        assertNotNull("No object returned.", response);
        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    }
}