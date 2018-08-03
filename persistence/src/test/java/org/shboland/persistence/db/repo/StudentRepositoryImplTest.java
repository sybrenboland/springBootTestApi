package org.shboland.persistence.db.repo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shboland.persistence.configuration.PersistenceTestConfiguration;
import org.shboland.persistence.criteria.StudentSearchCriteria;
import org.shboland.persistence.db.hibernate.bean.Student;
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
public class StudentRepositoryImplTest {

    @Autowired
    private StudentRepository studentRepository;

    // @InjectInput
    

    private static final String NAME = "string";
    private static final String NAME_DIFF = "other string";
    // @ParameterInput

    private Student student;
    private Student studentDiff;

    @Before
    public void setUp() {

        // @SubObjectCreationInput

        student = studentRepository.save(Student.builder()
                .name(NAME)
                // @FieldInput
                .build());

        studentDiff = studentRepository.save(Student.builder()
                .name(NAME)
                // @FieldInput
                .build());

        studentRepository.save(Student.builder()
                .name(NAME_DIFF)
                // @FieldInput
                .build());
                
        // @ObjectCreationInput
    }

    @After
    public void tearDown() {

        // @TearDownInputTop
        studentRepository.deleteAll();
        // @TearDownInputBottom
    }

    @Test
    public void testFindNumberOfStudentBySearchCriteria_WithTooMuchProperties() {

        StudentSearchCriteria searchCriteria = StudentSearchCriteria.builder()
                .id(Optional.of(studentDiff.getId()))
                .name(Optional.of(NAME_DIFF))
                // @CriteriaDiffInput
                .build();

        int result = studentRepository.findNumberOfStudentBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned with all properties!", 0, result);
    }

    @Test
    public void testFindNumberOfStudentBySearchCriteria_WithPerfectProperties() {

        StudentSearchCriteria searchCriteria = StudentSearchCriteria.builder()
                .id(Optional.of(student.getId()))
                .name(Optional.of(NAME))
                // @CriteriaInput
                .build();

        int result = studentRepository.findNumberOfStudentBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result);
    }

    @Test
    public void testFindNumberOfStudentBySearchCriteria_WithIdProperty() {

        StudentSearchCriteria searchCriteria = StudentSearchCriteria.builder()
                .id(Optional.of(studentDiff.getId()))
                .build();

        int result = studentRepository.findNumberOfStudentBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result);
    }

    @Test
    public void testFindBySearchCriteria_WithTooMuchProperties() {

        StudentSearchCriteria searchCriteria = StudentSearchCriteria.builder()
                .id(Optional.of(studentDiff.getId()))
                .name(Optional.of(NAME_DIFF))
                // @CriteriaDiffInput
                .build();

        List<Student> result = studentRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned with all properties!", 0, result.size());
    }

    @Test
    public void testFindBySearchCriteria_WithPerfectProperties() {

        StudentSearchCriteria searchCriteria = StudentSearchCriteria.builder()
                .id(Optional.of(student.getId()))
                .build();

        List<Student> result = studentRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result.size());
    }

    @Test
    public void testFindBySearchCriteria_WithIdProperty() {

        StudentSearchCriteria searchCriteria = StudentSearchCriteria.builder()
                .id(Optional.of(studentDiff.getId()))
                .build();

        List<Student> result = studentRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result.size());
    }

    // @Input

    @Test
    public void testFindNumberOfStudentBySearchCriteria_WithNameProperty() {

        StudentSearchCriteria searchCriteria = StudentSearchCriteria.builder()
                .name(Optional.of(NAME_DIFF))
                .build();

        int result = studentRepository.findNumberOfStudentBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result);

    }
            
    @Test
    public void testFindBySearchCriteria_WithNameProperty() {

        StudentSearchCriteria searchCriteria = StudentSearchCriteria.builder()
                .name(Optional.of(NAME_DIFF))
                .build();

        List<Student> result = studentRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result.size());

    }


}
