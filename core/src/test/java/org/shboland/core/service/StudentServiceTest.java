package org.shboland.core.service;

import org.shboland.persistence.db.hibernate.bean.Result;
import org.shboland.persistence.db.repo.ResultRepository;
import static org.mockito.Mockito.any;
import org.shboland.persistence.db.repo.DetailsRepository;
import org.shboland.persistence.db.hibernate.bean.Details;
import java.util.Collections;
import java.util.List;
import org.shboland.persistence.criteria.StudentSearchCriteria;
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
import org.shboland.persistence.db.hibernate.bean.Student;
import org.shboland.persistence.db.repo.StudentRepository;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StudentServiceTest {
    
    @InjectMocks
    private StudentService studentService;
    
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DetailsRepository detailsRepository;

    @Mock
    private ResultRepository resultRepository;

    // @MockInput
        
    private Result result = Result.builder().build();
        
    private static final long RESULT_ID = 3L;
        
    private Details details = Details.builder().build();
        
    private static final long DETAILS_ID = 3L;
        
    private static final int NUMBER_OF_STUDENTS = 2;
        
    private StudentSearchCriteria studentSearchCriteria = StudentSearchCriteria.builder().build();
        
    private Student student = Student.builder().build();
        
    private static final long STUDENT_ID = 3L;
    
    // @Input

    @Test
    public void testFetchStudentForResult_NoResultFound() {

        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.empty());

        Optional<Student> result = studentService.fetchStudentForResult(RESULT_ID);

        assertNotNull("No object returned!", result);
        assertFalse("Wrong object returned!", result.isPresent());
    }

    @Test
    public void testFetchStudentForResult_WithResultNoStudent() {

        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.of(result));

        Optional<Student> result = studentService.fetchStudentForResult(RESULT_ID);

        assertNotNull("No object returned!", result);
        assertFalse("Wrong object returned!", result.isPresent());
    }

    @Test
    public void testFetchStudentForResult_WithResultWithStudent() {

        Result resultWithStudent = result.toBuilder().student(student).build();
        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.of(resultWithStudent));

        Optional<Student> result = studentService.fetchStudentForResult(RESULT_ID);

        assertNotNull("No object returned!", result);
        assertTrue("Wrong object returned!", result.isPresent());
        assertEquals("Wrong object returned!", student, result.get());
    }
        
    @Test
    public void testRemoveResult_NoStudentFound() {

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.empty());

        boolean result = studentService.removeResult(STUDENT_ID, RESULT_ID);

        assertFalse("Wrong result returned!", result);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void testRemoveResult_StudentHasNoResult() {

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));

        boolean result = studentService.removeResult(STUDENT_ID, RESULT_ID);

        assertFalse("Wrong result returned!", result);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void testRemoveResult_NoResultFound() {

        Student studentWithResult = student.toBuilder().resultSet(Collections.singleton(result)).build();

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(studentWithResult));
        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.empty());

        boolean result = studentService.removeResult(STUDENT_ID, RESULT_ID);

        assertFalse("Wrong result returned!", result);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void testRemoveResult_ResultIdsDoNotMatch() {

        Student studentWithId = student.toBuilder().id(STUDENT_ID).build();
        Result resultWithStudent = result.toBuilder().id(RESULT_ID).student(student).build();

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(studentWithId));
        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.of(resultWithStudent));

        boolean result = studentService.removeResult(STUDENT_ID, RESULT_ID);

        assertFalse("Wrong result returned!", result);
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    public void testRemoveResult_ResultsMatch() {

        Student studentWithId = student.toBuilder().id(STUDENT_ID).build();
        Result resultWithStudent = result.toBuilder().id(RESULT_ID).student(studentWithId).build();

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(studentWithId));
        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.of(resultWithStudent));

        boolean result = studentService.removeResult(STUDENT_ID, RESULT_ID);

        assertTrue("Wrong result returned!", result);
        verify(resultRepository, times(1)).save(any(Result.class));
    }
        
    @Test
    public void testUpdateStudentWithResult_NoStudentFound() {

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.empty());

        boolean result = studentService.updateStudentWithResult(STUDENT_ID, RESULT_ID);

        assertFalse("Wrong result returned!", result);
        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    public void testUpdateStudentWithResult_NoResultFound() {

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.empty());

        boolean result = studentService.updateStudentWithResult(STUDENT_ID, RESULT_ID);

        assertFalse("Wrong result returned!", result);
        verify(resultRepository, never()).save(any(Result.class));
    }

    @Test
    public void testUpdateStudentWithResult_WithStudentWithResult() {

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(resultRepository.findById(RESULT_ID)).thenReturn(Optional.of(result));

        boolean result = studentService.updateStudentWithResult(STUDENT_ID, RESULT_ID);

        assertTrue("Wrong result returned!", result);
        verify(resultRepository, times(1)).save(any(Result.class));
    }
        
    @Test
    public void testUpdateStudentWithDetails_NoStudentFound() {

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.empty());

        boolean result = studentService.updateStudentWithDetails(STUDENT_ID, DETAILS_ID);

        assertFalse("Wrong result returned!", result);
        verify(detailsRepository, never()).save(any(Details.class));
    }

    @Test
    public void testUpdateStudentWithDetails_NoDetailsFound() {

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(detailsRepository.findById(DETAILS_ID)).thenReturn(Optional.empty());

        boolean result = studentService.updateStudentWithDetails(STUDENT_ID, DETAILS_ID);

        assertFalse("Wrong result returned!", result);
        verify(detailsRepository, never()).save(any(Details.class));
    }

    @Test
    public void testUpdateStudentWithDetails_WithStudentWithDetails() {

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));
        when(detailsRepository.findById(DETAILS_ID)).thenReturn(Optional.of(details));

        boolean result = studentService.updateStudentWithDetails(STUDENT_ID, DETAILS_ID);

        assertTrue("Wrong result returned!", result);
        verify(detailsRepository, times(1)).save(any(Details.class));
    }
    
    @Test
    public void testFindNumberOfStudent() {

        when(studentRepository.findNumberOfStudentBySearchCriteria(studentSearchCriteria)).thenReturn(NUMBER_OF_STUDENTS);

        int resultNumber = studentService.findNumberOfStudent(studentSearchCriteria);

        assertEquals("Wrong number returned!", NUMBER_OF_STUDENTS, resultNumber);
    } 
 
    @Test
    public void testFindBySearchCriteria() {

        when(studentRepository.findBySearchCriteria(studentSearchCriteria)).thenReturn(Collections.singletonList(student));

        List<Student> resultStudentList = studentService.findBySearchCriteria(studentSearchCriteria);

        assertNotNull("No object returned!", resultStudentList);
        assertEquals("Wrong number of objects returned!", 1, resultStudentList.size());
        assertEquals("Wrong object returned!", student, resultStudentList.get(0));
    }

    @Test
    public void testSaveStudent() {

        when(studentRepository.save(student)).thenReturn(student);

        Student savedStudent = studentService.save(student);

        assertNotNull("Wrong result returned!", savedStudent);
        assertEquals("Wrong object returned!", student, savedStudent);
    }

    @Test
    public void testFetchStudent() {

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));

        Optional<Student> fetchResult = studentService.fetchStudent(STUDENT_ID);

        assertTrue("Wrong result returned!", fetchResult.isPresent());
        assertEquals("Wrong object returned!", student, fetchResult.get());
    }
    
    @Test
    public void testDeleteStudent() {

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.of(student));

        boolean resultDelete = studentService.deleteStudent(STUDENT_ID);

        assertTrue("Wrong result returned!", resultDelete);
        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    public void testDeleteStudent_NoStudentFound() {

        when(studentRepository.findById(STUDENT_ID)).thenReturn(Optional.empty());

        boolean resultDelete = studentService.deleteStudent(STUDENT_ID);

        assertFalse("Wrong result returned!", resultDelete);
        verify(studentRepository, never()).delete(student);
    }
}