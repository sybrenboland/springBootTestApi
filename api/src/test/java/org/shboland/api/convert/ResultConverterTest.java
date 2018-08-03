package org.shboland.api.convert;

import org.junit.Before;
import org.junit.Test;
import org.shboland.domain.entities.JsonResult;
import org.shboland.persistence.db.hibernate.bean.Result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ResultConverterTest {

    private ResultConverter resultConverter = new ResultConverter();

    private static final Long ID = 3L;
    private static final Integer GRADE = 2;
    // @ParameterInput

    private Result result;
    private JsonResult jsonResult;

    @Before
    public void setUp() {

        this.result = Result.builder()
                .grade(GRADE)
                // @FieldInput
                .build();

        this.jsonResult = JsonResult.builder()
                .grade(GRADE)
                // @JsonFieldInput
                .build();
    }

    @Test
    public void testToJson() {

        JsonResult resultJsonResult = resultConverter.toJson(result);

        assertNotNull("No object returned.", resultJsonResult);
        assertEquals("Field not set correctly!", GRADE, resultJsonResult.getGrade());
        // @AssertJsonFieldInput
    }

    @Test
    public void testFromJson() {

        Result resultResult = resultConverter.fromJson(jsonResult);

        assertNotNull("No object returned.", resultResult);
        assertNull("Field not set correctly!", resultResult.getId());
        assertEquals("Field not set correctly!", GRADE, resultResult.getGrade());
        // @AssertFieldInput
    }

    @Test
    public void testFromJson_WithId() {

        Result resultResult = resultConverter.fromJson(jsonResult, ID);

        assertNotNull("No object returned.", resultResult);
        assertEquals("Field not set correctly.", ID, resultResult.getId());
        assertEquals("Field not set correctly!", GRADE, resultResult.getGrade());
        // @AssertFieldInput
    }
}