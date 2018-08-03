package org.shboland.api.convert;

import org.junit.Before;
import org.junit.Test;
import org.shboland.domain.entities.JsonCourseSearchCriteria;
import org.shboland.persistence.criteria.CourseSearchCriteria;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

public class CourseSearchCriteriaConverterTest {

    private CourseSearchCriteriaConverter courseSearchCriteriaConverter = new CourseSearchCriteriaConverter();

    private static final int MAX_RESULTS = 5;
    private static final int START = 1;
    private static final Long ID = 2L;
    private static final Boolean MASTER = false;
    // @ParameterInput

    private JsonCourseSearchCriteria jsonCourseSearchCriteria;

    @Before
    public void setUp() {

        jsonCourseSearchCriteria = JsonCourseSearchCriteria.builder()
                .start(START)
                .id(ID)
                .master(MASTER)
                // @FieldInput
                .build();
    }

    @Test
    public void testCreateSearchCriteria_NotPositiveMaxResults() {

        jsonCourseSearchCriteria = jsonCourseSearchCriteria.toBuilder()
                .maxResults(0)
                .build();

        try {
            courseSearchCriteriaConverter.createSearchCriteria(jsonCourseSearchCriteria);
            fail("An exception should be thrown!");
        } catch (ConvertException exception) {
            assertEquals("Wrong message returned!", "Maximum number of results should be a positive number.", exception.getMessage());
        }
    }

    @Test
    public void testCreateSearchCriteria() {

        jsonCourseSearchCriteria = jsonCourseSearchCriteria.toBuilder()
                .maxResults(MAX_RESULTS)
                .build();

        CourseSearchCriteria resultSearchCriteria = courseSearchCriteriaConverter
                .createSearchCriteria(jsonCourseSearchCriteria);

        assertNotNull("No object returned!", resultSearchCriteria);
        assertEquals("Field not set correctly!", MAX_RESULTS, resultSearchCriteria.getMaxResults());
        assertEquals("Field not set correctly!", START, resultSearchCriteria.getStart());
        assertTrue("Field not set correctly!", resultSearchCriteria.getId().isPresent());
        assertEquals("Field not set correctly!", ID, resultSearchCriteria.getId().get());
         assertTrue("Field not set correctly!", resultSearchCriteria.getMaster().isPresent());
        assertEquals("Field not set correctly!", MASTER, resultSearchCriteria.getMaster().get());
                // @AssertInput
    }

    // @Input

}
