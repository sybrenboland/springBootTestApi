package org.shboland.api.convert;

import org.junit.Before;
import org.junit.Test;
import org.shboland.domain.entities.JsonCourse;
import org.shboland.persistence.db.hibernate.bean.Course;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CourseConverterTest {

    private CourseConverter courseConverter = new CourseConverter();

    private static final Long ID = 3L;
    private static final Boolean MASTER = false;
    // @ParameterInput

    private Course course;
    private JsonCourse jsonCourse;

    @Before
    public void setUp() {

        this.course = Course.builder()
                .master(MASTER)
                // @FieldInput
                .build();

        this.jsonCourse = JsonCourse.builder()
                .master(MASTER)
                // @JsonFieldInput
                .build();
    }

    @Test
    public void testToJson() {

        JsonCourse resultJsonCourse = courseConverter.toJson(course);

        assertNotNull("No object returned.", resultJsonCourse);
        assertEquals("Field not set correctly!", MASTER, resultJsonCourse.getMaster());
        // @AssertJsonFieldInput
    }

    @Test
    public void testFromJson() {

        Course resultCourse = courseConverter.fromJson(jsonCourse);

        assertNotNull("No object returned.", resultCourse);
        assertNull("Field not set correctly!", resultCourse.getId());
        assertEquals("Field not set correctly!", MASTER, resultCourse.getMaster());
        // @AssertFieldInput
    }

    @Test
    public void testFromJson_WithId() {

        Course resultCourse = courseConverter.fromJson(jsonCourse, ID);

        assertNotNull("No object returned.", resultCourse);
        assertEquals("Field not set correctly.", ID, resultCourse.getId());
        assertEquals("Field not set correctly!", MASTER, resultCourse.getMaster());
        // @AssertFieldInput
    }
}