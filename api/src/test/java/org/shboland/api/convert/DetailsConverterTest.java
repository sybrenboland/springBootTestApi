package org.shboland.api.convert;

import org.junit.Before;
import org.junit.Test;
import org.shboland.domain.entities.JsonDetails;
import org.shboland.persistence.db.hibernate.bean.Details;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DetailsConverterTest {

    private DetailsConverter detailsConverter = new DetailsConverter();

    private static final Long ID = 3L;
    private static final Long NUMBER = 4L;
    // @ParameterInput

    private Details details;
    private JsonDetails jsonDetails;

    @Before
    public void setUp() {

        this.details = Details.builder()
                .number(NUMBER)
                // @FieldInput
                .build();

        this.jsonDetails = JsonDetails.builder()
                .number(NUMBER)
                // @JsonFieldInput
                .build();
    }

    @Test
    public void testToJson() {

        JsonDetails resultJsonDetails = detailsConverter.toJson(details);

        assertNotNull("No object returned.", resultJsonDetails);
        assertEquals("Field not set correctly!", NUMBER, resultJsonDetails.getNumber());
        // @AssertJsonFieldInput
    }

    @Test
    public void testFromJson() {

        Details resultDetails = detailsConverter.fromJson(jsonDetails);

        assertNotNull("No object returned.", resultDetails);
        assertNull("Field not set correctly!", resultDetails.getId());
        assertEquals("Field not set correctly!", NUMBER, resultDetails.getNumber());
        // @AssertFieldInput
    }

    @Test
    public void testFromJson_WithId() {

        Details resultDetails = detailsConverter.fromJson(jsonDetails, ID);

        assertNotNull("No object returned.", resultDetails);
        assertEquals("Field not set correctly.", ID, resultDetails.getId());
        assertEquals("Field not set correctly!", NUMBER, resultDetails.getNumber());
        // @AssertFieldInput
    }
}