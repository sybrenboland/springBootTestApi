package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shboland.persistence.configuration.PersistenceTestConfiguration;
import org.shboland.persistence.criteria.ResultSearchCriteria;
import org.shboland.persistence.db.hibernate.bean.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = { PersistenceTestConfiguration.class })
public class ResultRepositoryImplTest {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private StudentRepository studentRepository;
    
    // @InjectInput
    

    private static final Integer GRADE = 2;
    private static final Integer GRADE_DIFF = 3;
    // @ParameterInput
            
    private Student student;
    private Student studentDiff;

    private Result result;
    private Result resultDiff;

    @Before
    public void setUp() {

        // @SubObjectCreationInput      
              
        student = studentRepository.save(Student.builder().build());
        studentDiff = studentRepository.save(Student.builder().build());

        result = resultRepository.save(Result.builder()
                .grade(GRADE)
                .student(student)
                // @FieldInput
                .build());

        resultDiff = resultRepository.save(Result.builder()
                .grade(GRADE)
                .student(student)
                // @FieldInput
                .build());

        resultRepository.save(Result.builder()
                .grade(GRADE_DIFF)
                .student(student)
                // @FieldInput
                .build());
                
        resultRepository.save(Result.builder()
                .grade(GRADE)
                .student(studentDiff)
                // @FieldInput
                .build());
                
        // @ObjectCreationInput
    }

    @After
    public void tearDown() {

        // @TearDownInputTop
        resultRepository.deleteAll();
        studentRepository.deleteAll();
                // @TearDownInputBottom
    }

    @Test
    public void testFindNumberOfResultBySearchCriteria_WithTooMuchProperties() {

        ResultSearchCriteria searchCriteria = ResultSearchCriteria.builder()
                .id(Optional.of(resultDiff.getId()))
                .grade(Optional.of(GRADE_DIFF))
                .studentId(Optional.of(studentDiff.getId()))
                // @CriteriaDiffInput
                .build();

        int result = resultRepository.findNumberOfResultBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned with all properties!", 0, result);
    }

    @Test
    public void testFindNumberOfResultBySearchCriteria_WithPerfectProperties() {

        ResultSearchCriteria searchCriteria = ResultSearchCriteria.builder()
                .id(Optional.of(result.getId()))
                .grade(Optional.of(GRADE))
                .studentId(Optional.of(student.getId()))
                // @CriteriaInput
                .build();

        int result = resultRepository.findNumberOfResultBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result);
    }

    @Test
    public void testFindNumberOfResultBySearchCriteria_WithIdProperty() {

        ResultSearchCriteria searchCriteria = ResultSearchCriteria.builder()
                .id(Optional.of(resultDiff.getId()))
                .build();

        int result = resultRepository.findNumberOfResultBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result);
    }

    @Test
    public void testFindBySearchCriteria_WithTooMuchProperties() {

        ResultSearchCriteria searchCriteria = ResultSearchCriteria.builder()
                .id(Optional.of(resultDiff.getId()))
                .grade(Optional.of(GRADE_DIFF))
                .studentId(Optional.of(studentDiff.getId()))
                // @CriteriaDiffInput
                .build();

        List<Result> result = resultRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned with all properties!", 0, result.size());
    }

    @Test
    public void testFindBySearchCriteria_WithPerfectProperties() {

        ResultSearchCriteria searchCriteria = ResultSearchCriteria.builder()
                .id(Optional.of(result.getId()))
                .build();

        List<Result> result = resultRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result.size());
    }

    @Test
    public void testFindBySearchCriteria_WithIdProperty() {

        ResultSearchCriteria searchCriteria = ResultSearchCriteria.builder()
                .id(Optional.of(resultDiff.getId()))
                .build();

        List<Result> result = resultRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result.size());
    }

    // @Input

    @Test
    public void testFindNumberOfResultBySearchCriteria_WithStudentProperty() {

        ResultSearchCriteria searchCriteria = ResultSearchCriteria.builder()
                .studentId(Optional.of(studentDiff.getId()))
                .build();

        int result = resultRepository.findNumberOfResultBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result);
    }
            
    @Test
    public void testFindBySearchCriteria_WithStudentProperty() {

        ResultSearchCriteria searchCriteria = ResultSearchCriteria.builder()
                .studentId(Optional.of(studentDiff.getId()))
                .build();

        List<Result> result = resultRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result.size());
    }



    @Test
    public void testFindNumberOfResultBySearchCriteria_WithGradeProperty() {

        ResultSearchCriteria searchCriteria = ResultSearchCriteria.builder()
                .grade(Optional.of(GRADE_DIFF))
                .build();

        int result = resultRepository.findNumberOfResultBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result);

    }
            
    @Test
    public void testFindBySearchCriteria_WithGradeProperty() {

        ResultSearchCriteria searchCriteria = ResultSearchCriteria.builder()
                .grade(Optional.of(GRADE_DIFF))
                .build();

        List<Result> result = resultRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result.size());

    }


}
