package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shboland.persistence.configuration.PersistenceTestConfiguration;
import org.shboland.persistence.criteria.DetailsSearchCriteria;
import org.shboland.persistence.db.hibernate.bean.Details;
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
public class DetailsRepositoryImplTest {

    @Autowired
    private DetailsRepository detailsRepository;

    // @InjectInput
    

    private static final Long NUMBER = 4L;
    private static final Long NUMBER_DIFF = 5L;
    // @ParameterInput

    private Details details;
    private Details detailsDiff;

    @Before
    public void setUp() {

        // @SubObjectCreationInput

        details = detailsRepository.save(Details.builder()
                .number(NUMBER)
                .student(Student.builder().build())
                // @FieldInput
                .build());

        detailsDiff = detailsRepository.save(Details.builder()
                .number(NUMBER)
                .student(Student.builder().build())
                // @FieldInput
                .build());

        detailsRepository.save(Details.builder()
                .number(NUMBER_DIFF)
                .student(Student.builder().build())
                // @FieldInput
                .build());
                
        // @ObjectCreationInput
    }

    @After
    public void tearDown() {

        // @TearDownInputTop
        detailsRepository.deleteAll();
        // @TearDownInputBottom
    }

    @Test
    public void testFindNumberOfDetailsBySearchCriteria_WithTooMuchProperties() {

        DetailsSearchCriteria searchCriteria = DetailsSearchCriteria.builder()
                .id(Optional.of(detailsDiff.getId()))
                .number(Optional.of(NUMBER_DIFF))
                // @CriteriaDiffInput
                .build();

        int result = detailsRepository.findNumberOfDetailsBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned with all properties!", 0, result);
    }

    @Test
    public void testFindNumberOfDetailsBySearchCriteria_WithPerfectProperties() {

        DetailsSearchCriteria searchCriteria = DetailsSearchCriteria.builder()
                .id(Optional.of(details.getId()))
                .number(Optional.of(NUMBER))
                // @CriteriaInput
                .build();

        int result = detailsRepository.findNumberOfDetailsBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result);
    }

    @Test
    public void testFindNumberOfDetailsBySearchCriteria_WithIdProperty() {

        DetailsSearchCriteria searchCriteria = DetailsSearchCriteria.builder()
                .id(Optional.of(detailsDiff.getId()))
                .build();

        int result = detailsRepository.findNumberOfDetailsBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result);
    }

    @Test
    public void testFindBySearchCriteria_WithTooMuchProperties() {

        DetailsSearchCriteria searchCriteria = DetailsSearchCriteria.builder()
                .id(Optional.of(detailsDiff.getId()))
                .number(Optional.of(NUMBER_DIFF))
                // @CriteriaDiffInput
                .build();

        List<Details> result = detailsRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned with all properties!", 0, result.size());
    }

    @Test
    public void testFindBySearchCriteria_WithPerfectProperties() {

        DetailsSearchCriteria searchCriteria = DetailsSearchCriteria.builder()
                .id(Optional.of(details.getId()))
                .build();

        List<Details> result = detailsRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result.size());
    }

    @Test
    public void testFindBySearchCriteria_WithIdProperty() {

        DetailsSearchCriteria searchCriteria = DetailsSearchCriteria.builder()
                .id(Optional.of(detailsDiff.getId()))
                .build();

        List<Details> result = detailsRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result.size());
    }

    // @Input

    @Test
    public void testFindNumberOfDetailsBySearchCriteria_WithNumberProperty() {

        DetailsSearchCriteria searchCriteria = DetailsSearchCriteria.builder()
                .number(Optional.of(NUMBER_DIFF))
                .build();

        int result = detailsRepository.findNumberOfDetailsBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result);

    }
            
    @Test
    public void testFindBySearchCriteria_WithNumberProperty() {

        DetailsSearchCriteria searchCriteria = DetailsSearchCriteria.builder()
                .number(Optional.of(NUMBER_DIFF))
                .build();

        List<Details> result = detailsRepository.findBySearchCriteria(searchCriteria);

        assertEquals("Wrong number of objects returned!", 1, result.size());

    }


}
