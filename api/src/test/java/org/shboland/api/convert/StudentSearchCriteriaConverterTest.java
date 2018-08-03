package org.shboland.api.convert;

import org.junit.Before;
import org.junit.Test;
import org.shboland.domain.entities.JsonStudentSearchCriteria;
import org.shboland.persistence.criteria.StudentSearchCriteria;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

public class StudentSearchCriteriaConverterTest {

    private StudentSearchCriteriaConverter studentSearchCriteriaConverter = new StudentSearchCriteriaConverter();

    private static final int MAX_RESULTS = 5;
    private static final int START = 1;
    private static final Long ID = 2L;
    private static final String NAME = "string";
    // @ParameterInput

    private JsonStudentSearchCriteria jsonStudentSearchCriteria;

    @Before
    public void setUp() {

        jsonStudentSearchCriteria = JsonStudentSearchCriteria.builder()
                .start(START)
                .id(ID)
                .name(NAME)
                // @FieldInput
                .build();
    }

    @Test
    public void testCreateSearchCriteria_NotPositiveMaxResults() {

        jsonStudentSearchCriteria = jsonStudentSearchCriteria.toBuilder()
                .maxResults(0)
                .build();

        try {
            studentSearchCriteriaConverter.createSearchCriteria(jsonStudentSearchCriteria);
            fail("An exception should be thrown!");
        } catch (ConvertException exception) {
            assertEquals("Wrong message returned!", "Maximum number of results should be a positive number.", exception.getMessage());
        }
    }

    @Test
    public void testCreateSearchCriteria() {

        jsonStudentSearchCriteria = jsonStudentSearchCriteria.toBuilder()
                .maxResults(MAX_RESULTS)
                .build();

        StudentSearchCriteria resultSearchCriteria = studentSearchCriteriaConverter
                .createSearchCriteria(jsonStudentSearchCriteria);

        assertNotNull("No object returned!", resultSearchCriteria);
        assertEquals("Field not set correctly!", MAX_RESULTS, resultSearchCriteria.getMaxResults());
        assertEquals("Field not set correctly!", START, resultSearchCriteria.getStart());
        assertTrue("Field not set correctly!", resultSearchCriteria.getId().isPresent());
        assertEquals("Field not set correctly!", ID, resultSearchCriteria.getId().get());
         assertTrue("Field not set correctly!", resultSearchCriteria.getName().isPresent());
        assertEquals("Field not set correctly!", NAME, resultSearchCriteria.getName().get());
                // @AssertInput
    }

    // @Input

}
