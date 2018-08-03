package org.shboland.api.convert;

import org.junit.Before;
import org.junit.Test;
import org.shboland.domain.entities.JsonResultSearchCriteria;
import org.shboland.persistence.criteria.ResultSearchCriteria;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

public class ResultSearchCriteriaConverterTest {

    private ResultSearchCriteriaConverter resultSearchCriteriaConverter = new ResultSearchCriteriaConverter();

    private static final int MAX_RESULTS = 5;
    private static final int START = 1;
    private static final Long ID = 2L;
    private static final Integer GRADE = 2;
    // @ParameterInput

    private JsonResultSearchCriteria jsonResultSearchCriteria;

    @Before
    public void setUp() {

        jsonResultSearchCriteria = JsonResultSearchCriteria.builder()
                .start(START)
                .id(ID)
                .grade(GRADE)
                // @FieldInput
                .build();
    }

    @Test
    public void testCreateSearchCriteria_NotPositiveMaxResults() {

        jsonResultSearchCriteria = jsonResultSearchCriteria.toBuilder()
                .maxResults(0)
                .build();

        try {
            resultSearchCriteriaConverter.createSearchCriteria(jsonResultSearchCriteria);
            fail("An exception should be thrown!");
        } catch (ConvertException exception) {
            assertEquals("Wrong message returned!", "Maximum number of results should be a positive number.", exception.getMessage());
        }
    }

    @Test
    public void testCreateSearchCriteria() {

        jsonResultSearchCriteria = jsonResultSearchCriteria.toBuilder()
                .maxResults(MAX_RESULTS)
                .build();

        ResultSearchCriteria resultSearchCriteria = resultSearchCriteriaConverter
                .createSearchCriteria(jsonResultSearchCriteria);

        assertNotNull("No object returned!", resultSearchCriteria);
        assertEquals("Field not set correctly!", MAX_RESULTS, resultSearchCriteria.getMaxResults());
        assertEquals("Field not set correctly!", START, resultSearchCriteria.getStart());
        assertTrue("Field not set correctly!", resultSearchCriteria.getId().isPresent());
        assertEquals("Field not set correctly!", ID, resultSearchCriteria.getId().get());
         assertTrue("Field not set correctly!", resultSearchCriteria.getGrade().isPresent());
        assertEquals("Field not set correctly!", GRADE, resultSearchCriteria.getGrade().get());
                // @AssertInput
    }

    // @Input

}
