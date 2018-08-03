package org.shboland.core.service;

import static org.mockito.Mockito.any;
import org.shboland.persistence.db.repo.StudentRepository;
import org.shboland.persistence.db.hibernate.bean.Student;
import java.util.Collections;
import java.util.List;
import org.shboland.persistence.criteria.ResultSearchCriteria;
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
import org.shboland.persistence.db.hibernate.bean.Result;
import org.shboland.persistence.db.repo.ResultRepository;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResultServiceTest {
    
    @InjectMocks
    private ResultService resultService;
    
    @Mock
    private ResultRepository resultRepository;

    @Mock
    private StudentRepository studentRepository;

    // @MockInput
        
    private Student student = Student.builder().build();
        
    private static final long STUDENT_ID = 3L;
        
    private static final int NUMBER_OF_RESULTS = 2;
        
    private ResultSearchCriteria resultSearchCriteria = ResultSearchCriteria.builder().build();
        
    private Result result = Result.builder().build();
        
    private static final long RESULT_ID = 3L;
    
    // @Input

    @Test
    public void testFetchResultsForStudent() {

        when(resultRepository.findBySearchCriteria(any())).thenReturn(Collections.singletonList(result));

        List<Result> resultResultList = resultService.fetchResultsForStudent(RESULT_ID);

        assertNotNull("No object returned!", resultResultList);
        assertEquals("Wrong number of objects returned!", 1, resultResultList.size());
        assertEquals("Wrong object returned!", result, resultResultList.get(0));
    }
        
    @Test
    public void testRemoveStudent_NoResultFound() {

        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.empty());

        boolean result = resultService.removeStudent(RESULT_ID, STUDENT_ID);

        assertFalse("Wrong result returned!", result);
        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    public void testRemoveStudent_ResultHasNoStudent() {

        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.of(result));

        boolean result = resultService.removeStudent(RESULT_ID, STUDENT_ID);

        assertFalse("Wrong result returned!", result);
        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    public void testRemoveStudent_NoStudentFound() {

        Result resultWithStudent = result.toBuilder().student(student).build();

        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.of(resultWithStudent));
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.empty());

        boolean result = resultService.removeStudent(RESULT_ID, STUDENT_ID);

        assertFalse("Wrong result returned!", result);
        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    public void testRemoveStudent_StudentIdsDoNotMatch() {

        Student studentWithId = student.toBuilder().id(STUDENT_ID).build();
        Result resultWithStudent = result.toBuilder().student(Student.builder().id(STUDENT_ID + 1).build()).build();

        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.of(resultWithStudent));
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(studentWithId));

        boolean result = resultService.removeStudent(RESULT_ID, STUDENT_ID);

        assertFalse("Wrong result returned!", result);
        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    public void testRemoveStudent_StudentsMatch() {

        Student studentWithId = student.toBuilder().id(STUDENT_ID).build();
        Result resultWithStudent = result.toBuilder().student(studentWithId).build();

        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.of(resultWithStudent));
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(studentWithId));

        boolean result = resultService.removeStudent(RESULT_ID, STUDENT_ID);

        assertTrue("Wrong result returned!", result);
        verify(resultRepository, times(1)).save(any(Result.class));
    }
        
    @Test
    public void testUpdateResultWithStudent_NoResultFound() {

        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.empty());

        boolean result = resultService.updateResultWithStudent(RESULT_ID, STUDENT_ID);

        assertFalse("Wrong result returned!", result);
        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    public void testUpdateResultWithStudent_NoStudentFound() {

        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.of(result));
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.empty());

        boolean result = resultService.updateResultWithStudent(RESULT_ID, STUDENT_ID);

        assertFalse("Wrong result returned!", result);
        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    public void testUpdateResultWithStudent_WithResultWithStudent() {

        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.of(result));
        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));

        boolean result = resultService.updateResultWithStudent(RESULT_ID, STUDENT_ID);

        assertTrue("Wrong result returned!", result);
        verify(resultRepository, times(1)).save(any(Result.class));
    }
    
    @Test
    public void testFindNumberOfResult() {

        when(resultRepository.findNumberOfResultBySearchCriteria(resultSearchCriteria)).thenReturn(NUMBER_OF_RESULTS);

        int resultNumber = resultService.findNumberOfResult(resultSearchCriteria);

        assertEquals("Wrong number returned!", NUMBER_OF_RESULTS, resultNumber);
    } 
 
    @Test
    public void testFindBySearchCriteria() {

        when(resultRepository.findBySearchCriteria(resultSearchCriteria)).thenReturn(Collections.singletonList(result));

        List<Result> resultResultList = resultService.findBySearchCriteria(resultSearchCriteria);

        assertNotNull("No object returned!", resultResultList);
        assertEquals("Wrong number of objects returned!", 1, resultResultList.size());
        assertEquals("Wrong object returned!", result, resultResultList.get(0));
    }

    @Test
    public void testSaveResult() {

        when(resultRepository.save(result)).thenReturn(result);

        Result savedResult = resultService.save(result);

        assertNotNull("Wrong result returned!", savedResult);
        assertEquals("Wrong object returned!", result, savedResult);
    }

    @Test
    public void testFetchResult() {

        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.of(result));

        Optional<Result> fetchResult = resultService.fetchResult(RESULT_ID);

        assertTrue("Wrong result returned!", fetchResult.isPresent());
        assertEquals("Wrong object returned!", result, fetchResult.get());
    }
    
    @Test
    public void testDeleteResult() {

        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.of(result));

        boolean resultDelete = resultService.deleteResult(RESULT_ID);

        assertTrue("Wrong result returned!", resultDelete);
        verify(resultRepository, times(1)).delete(result);
    }

    @Test
    public void testDeleteResult_NoResultFound() {

        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.empty());

        boolean resultDelete = resultService.deleteResult(RESULT_ID);

        assertFalse("Wrong result returned!", resultDelete);
        verify(resultRepository, never()).delete(result);
    }
}