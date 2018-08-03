package org.shboland.persistence.db.repo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shboland.persistence.configuration.PersistenceTestConfiguration;
import org.shboland.persistence.criteria.CourseSearchCriteria;
import org.shboland.persistence.db.hibernate.bean.Course;
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
public class CourseRepositoryImplTest {

    @Autowired
    private CourseRepository courseRepository;

    // @InjectInput
    

    private static final Boolean MASTER = false;
    private static final Boolean MASTER_DIFF = true;
    // @ParameterInput

    private Course course;
    private Course courseDiff;

    @Before
    public void setUp() {

        // @SubObjectCreationInput

        course = courseRepository.save(Course.builder()
                .master(MASTER)
                // @FieldInput
                .build());

        courseDiff = courseRepository.save(Course.builder()
                .master(MASTER)
                // @FieldInput
                .build());

        courseRepository.save(Course.builder()
                .master(MASTER_DIFF)
                // @FieldInput
                .build());
                
        // @ObjectCreationInput
    }

    @After
    public void tearDown() {

        // @TearDownInputTop
        courseRepository.deleteAll();
        // @TearDownInputBottom
    }

    @Test
    public void testFindNumberOfCourseBySearchCriteria_WithTooMuchProperties() {

        CourseSearchCriteria searchCriteria = CourseSearchCriteria.builder()
                .id(Optional.of(courseDiff.getId()))
                .master(Optional.of(MASTER_DIFF))
                // @CriteriaDiffInput
                .build();

        int result = courseRepository.findNumberOfCourseBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned with all properties!", 0, result);
    }

    @Test
    public void testFindNumberOfCourseBySearchCriteria_WithPerfectProperties() {

        CourseSearchCriteria searchCriteria = CourseSearchCriteria.builder()
                .id(Optional.of(course.getId()))
                .master(Optional.of(MASTER))
                // @CriteriaInput
                .build();

        int result = courseRepository.findNumberOfCourseBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result);
    }

    @Test
    public void testFindNumberOfCourseBySearchCriteria_WithIdProperty() {

        CourseSearchCriteria searchCriteria = CourseSearchCriteria.builder()
                .id(Optional.of(courseDiff.getId()))
                .build();

        int result = courseRepository.findNumberOfCourseBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result);
    }

    @Test
    public void testFindBySearchCriteria_WithTooMuchProperties() {

        CourseSearchCriteria searchCriteria = CourseSearchCriteria.builder()
                .id(Optional.of(courseDiff.getId()))
                .master(Optional.of(MASTER_DIFF))
                // @CriteriaDiffInput
                .build();

        List<Course> result = courseRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned with all properties!", 0, result.size());
    }

    @Test
    public void testFindBySearchCriteria_WithPerfectProperties() {

        CourseSearchCriteria searchCriteria = CourseSearchCriteria.builder()
                .id(Optional.of(course.getId()))
                .build();

        List<Course> result = courseRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result.size());
    }

    @Test
    public void testFindBySearchCriteria_WithIdProperty() {

        CourseSearchCriteria searchCriteria = CourseSearchCriteria.builder()
                .id(Optional.of(courseDiff.getId()))
                .build();

        List<Course> result = courseRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result.size());
    }

    // @Input

    @Test
    public void testFindNumberOfCourseBySearchCriteria_WithMasterProperty() {

        CourseSearchCriteria searchCriteria = CourseSearchCriteria.builder()
                .master(Optional.of(MASTER_DIFF))
                .build();

        int result = courseRepository.findNumberOfCourseBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result);

    }
            
    @Test
    public void testFindBySearchCriteria_WithMasterProperty() {

        CourseSearchCriteria searchCriteria = CourseSearchCriteria.builder()
                .master(Optional.of(MASTER_DIFF))
                .build();

        List<Course> result = courseRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result.size());

    }


}
