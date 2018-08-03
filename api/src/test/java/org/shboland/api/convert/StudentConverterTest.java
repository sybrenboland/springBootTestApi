package org.shboland.api.convert;

import org.junit.Before;
import org.junit.Test;
import org.shboland.domain.entities.JsonStudent;
import org.shboland.persistence.db.hibernate.bean.Student;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StudentConverterTest {

    private StudentConverter studentConverter = new StudentConverter();

    private static final Long ID = 3L;
    private static final String NAME = "string";
    // @ParameterInput

    private Student student;
    private JsonStudent jsonStudent;

    @Before
    public void setUp() {

        this.student = Student.builder()
                .name(NAME)
                // @FieldInput
                .build();

        this.jsonStudent = JsonStudent.builder()
                .name(NAME)
                // @JsonFieldInput
                .build();
    }

    @Test
    public void testToJson() {

        JsonStudent resultJsonStudent = studentConverter.toJson(student);

        assertNotNull("No object returned.", resultJsonStudent);
        assertEquals("Field not set correctly!", NAME, resultJsonStudent.getName());
        // @AssertJsonFieldInput
    }

    @Test
    public void testFromJson() {

        Student resultStudent = studentConverter.fromJson(jsonStudent);

        assertNotNull("No object returned.", resultStudent);
        assertNull("Field not set correctly!", resultStudent.getId());
        assertEquals("Field not set correctly!", NAME, resultStudent.getName());
        // @AssertFieldInput
    }

    @Test
    public void testFromJson_WithId() {

        Student resultStudent = studentConverter.fromJson(jsonStudent, ID);

        assertNotNull("No object returned.", resultStudent);
        assertEquals("Field not set correctly.", ID, resultStudent.getId());
        assertEquals("Field not set correctly!", NAME, resultStudent.getName());
        // @AssertFieldInput
    }
}