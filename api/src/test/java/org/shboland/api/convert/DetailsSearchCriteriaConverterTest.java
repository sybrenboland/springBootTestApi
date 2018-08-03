package org.shboland.api.convert;

import org.junit.Before;
import org.junit.Test;
import org.shboland.domain.entities.JsonDetailsSearchCriteria;
import org.shboland.persistence.criteria.DetailsSearchCriteria;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

public class DetailsSearchCriteriaConverterTest {

    private DetailsSearchCriteriaConverter detailsSearchCriteriaConverter = new DetailsSearchCriteriaConverter();

    private static final int MAX_RESULTS = 5;
    private static final int START = 1;
    private static final Long ID = 2L;
    private static final Long NUMBER = 4L;
    // @ParameterInput

    private JsonDetailsSearchCriteria jsonDetailsSearchCriteria;

    @Before
    public void setUp() {

        jsonDetailsSearchCriteria = JsonDetailsSearchCriteria.builder()
                .start(START)
                .id(ID)
                .number(NUMBER)
                // @FieldInput
                .build();
    }

    @Test
    public void testCreateSearchCriteria_NotPositiveMaxResults() {

        jsonDetailsSearchCriteria = jsonDetailsSearchCriteria.toBuilder()
                .maxResults(0)
                .build();

        try {
            detailsSearchCriteriaConverter.createSearchCriteria(jsonDetailsSearchCriteria);
            fail("An exception should be thrown!");
        } catch (ConvertException exception) {
            assertEquals("Wrong message returned!", "Maximum number of results should be a positive number.", exception.getMessage());
        }
    }

    @Test
    public void testCreateSearchCriteria() {

        jsonDetailsSearchCriteria = jsonDetailsSearchCriteria.toBuilder()
                .maxResults(MAX_RESULTS)
                .build();

        DetailsSearchCriteria resultSearchCriteria = detailsSearchCriteriaConverter
                .createSearchCriteria(jsonDetailsSearchCriteria);

        assertNotNull("No object returned!", resultSearchCriteria);
        assertEquals("Field not set correctly!", MAX_RESULTS, resultSearchCriteria.getMaxResults());
        assertEquals("Field not set correctly!", START, resultSearchCriteria.getStart());
        assertTrue("Field not set correctly!", resultSearchCriteria.getId().isPresent());
        assertEquals("Field not set correctly!", ID, resultSearchCriteria.getId().get());
         assertTrue("Field not set correctly!", resultSearchCriteria.getNumber().isPresent());
        assertEquals("Field not set correctly!", NUMBER, resultSearchCriteria.getNumber().get());
                // @AssertInput
    }

    // @Input

}
