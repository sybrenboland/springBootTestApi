package integration;

import org.shboland.persistence.db.repo.ResultRepository;
import org.shboland.persistence.db.hibernate.bean.Result;
import java.util.ArrayList;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.shboland.persistence.db.repo.DetailsRepository;
import org.shboland.persistence.db.hibernate.bean.Details;
import org.shboland.domain.entities.JsonStudent;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.http.HttpStatus;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.shboland.api.Application;
import org.shboland.persistence.db.hibernate.bean.Student;
import org.shboland.persistence.db.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class StudentResourceIT {

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private StudentRepository studentRepository;

    
    @Autowired
    private DetailsRepository detailsRepository;

     
    @Autowired
    private ResultRepository resultRepository;

     // @InjectInput

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() {
        // @TearDownInputTop
      
        resultRepository.deleteAll();
      
        detailsRepository.deleteAll();
        studentRepository.deleteAll();
        // @TearDownInputBottom
    }

    // @Input

    @Test
    public void testGetResults_withStudentWithResults() throws Exception {
    
        Student student = IntegrationTestFactory.givenAStudentWithResult(studentRepository, resultRepository);
            IntegrationTestFactory.givenAResult(resultRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/students/" + student.getId() + "/results"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":1"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":1"));
    }

    @Test
    public void testGetResults_withStudentNoResults() throws Exception {
    
        Student student = IntegrationTestFactory.givenAStudent(studentRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/students/" + student.getId() + "/results"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":0"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":0"));
        assertTrue("Wrong entities returned.", response.getContentAsString().contains("\"results\":[]"));
    }

    @Test
    public void testGetResults_withoutStudent() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/students/-1/results"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":0"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":0"));
        assertTrue("Wrong entities returned.", response.getContentAsString().contains("\"results\":[]"));
    }

    @Test
    public void testDeleteResult_withStudentWithResults() throws Exception {
    
        Student student = IntegrationTestFactory.givenAStudentWithResult(studentRepository, resultRepository);
        Result result = new ArrayList<>(student.getResultSet()).get(0);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/students/" + student.getId() + "/results/" + result.getId()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        assertFalse("Wrong entity link returned.",
                mockMvc.perform(MockMvcRequestBuilders.get("/students/" + student.getId() + "/results"))
                        .andReturn().getResponse().getContentAsString()
                        .contains("/results/" + result.getId()));
    }

    @Test
    public void testDeleteResult_withStudentNoResults() throws Exception {
    
        Student student = IntegrationTestFactory.givenAStudent(studentRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/students/" + student.getId() + "/results/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteResult_withoutStudent() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("students/-1/results/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        

        mockMvc.perform(MockMvcRequestBuilders.delete("/students/-1/results/-1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    
    @Test
    public void testPutResult_withStudent() throws Exception {

        Student student = IntegrationTestFactory.givenAStudent(studentRepository);
        Result result = IntegrationTestFactory.givenAResult(resultRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/students/" + student.getId() + "/results/" + result.getId()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        assertTrue("Wrong entity link returned.",
                mockMvc.perform(MockMvcRequestBuilders.get("/students/" + student.getId() + "/results"))
                        .andReturn().getResponse().getContentAsString()
                        .contains("/results/" + result.getId()));
    }
    
    @Test
    public void testPutResult_withStudentWithResult() throws Exception {
    
        Student student = IntegrationTestFactory.givenAStudentWithResult(studentRepository, resultRepository);
        Result result = new ArrayList<>(student.getResultSet()).get(0);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/students/" + student.getId() + "/results/" + result.getId()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        assertTrue("Wrong entity link returned.",
                mockMvc.perform(MockMvcRequestBuilders.get("/students/" + student.getId() + "/results"))
                        .andReturn().getResponse().getContentAsString()
                        .contains("/results/" + result.getId()));
    }

    @Test
    public void testPutResult_withStudentNoResult() throws Exception {
    
        Student student = IntegrationTestFactory.givenAStudent(studentRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/students/" + student.getId() + "/results/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testPutResult_withoutStudent() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/students/-1/results/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteDetails_withStudentWithDetails() throws Exception {
    
        Details details = IntegrationTestFactory.givenADetails(detailsRepository);
        Student student = details.getStudent();

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/students/" + student.getId() + "/details"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteDetails_withStudentNoDetails() throws Exception {
    
        Student student = IntegrationTestFactory.givenAStudent(studentRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/students/" + student.getId() + "/details"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteDetails_withoutStudent() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("students/-1/details"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        

        mockMvc.perform(MockMvcRequestBuilders.delete("/students/-1/details"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    
    @Test
    public void testPutDetails_withStudentNoDetails() throws Exception {
    
        Student student = IntegrationTestFactory.givenAStudent(studentRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/students/" + student.getId() + "/detailss/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testPutDetails_withoutStudent() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/students/-1/detailss/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testGetDetails_withStudentWithDetails() throws Exception {
    
        Details details = IntegrationTestFactory.givenADetails(detailsRepository);
        Student student = details.getStudent();

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/students/" + student.getId() + "/details"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/detailss/" + details.getId()));
    }
    
            @Test
    public void testGetDetails_withStudentNoDetails() throws Exception {
    
        Student student = IntegrationTestFactory.givenAStudent(studentRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/students/" + student.getId() + "/details"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }
    
    @Test
    public void testGetDetails_withoutStudent() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/students/-1/details"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testGetStudent_withStudent() throws Exception {

        Student student = IntegrationTestFactory.givenAStudent(studentRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/students/" + student.getId()))
                        .andReturn().getResponse();
                        
        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/students/" + student.getId()));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"name\":" + "\"" + student.getName() + "\""));
        // @FieldInputAssert
    }

    @Test
    public void testGetStudent_withoutStudent() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/students/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testList_withoutStudents() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/students"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":0"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":0"));
        assertTrue("Wrong entities returned.", response.getContentAsString().contains("\"results\":[]"));
    }

    @Test
    public void testList_withStudents() throws Exception {
    
        Student savedStudent = IntegrationTestFactory.givenAStudent(studentRepository);
        IntegrationTestFactory.givenAStudent(studentRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/students"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":2"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":2"));
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("students/" + savedStudent.getId()));
    }

        
    @Test
    public void testPostStudent_invalidObject() throws Exception {
    
         MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.post("/students"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testPostStudent_newObject() throws Exception {
    
        JsonStudent student = IntegrationTestFactory.givenAJsonStudent();

        MockHttpServletResponse response =
                mockMvc.perform(IntegrationTestUtils.doPost("/students", student))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.CREATED.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/students/"));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"name\":" + "\"" + student.getName() + "\""));
        // @FieldInputAssert
    }

    @Test
    public void testPutStudent_invalidObject() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/students/-1", new Object()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testPutStudent_newObject() throws Exception {
    
        JsonStudent student = IntegrationTestFactory.givenAJsonStudent();

        MockHttpServletResponse response =
                mockMvc.perform(IntegrationTestUtils.doPut("/students/-1", student))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/students/"));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"name\":" + "\"" + student.getName() + "\""));
        // @FieldInputAssert
    }

    @Test
    public void testPutStudent_updateObject() throws Exception {
    
        Student savedStudent = IntegrationTestFactory.givenAStudent(studentRepository);

        JsonStudent student = IntegrationTestFactory.givenAJsonStudent();

        MockHttpServletResponse response =
                mockMvc.perform(IntegrationTestUtils.doPut("/students/" + savedStudent.getId(), student))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/students/"));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"name\":" + "\"" + student.getName() + "\""));
        // @FieldInputAssert
    }

    @Test
    public void testDeleteStudent_unknownObject() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/students/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteStudent_deleteObject() throws Exception {
    
        Student savedStudent = IntegrationTestFactory.givenAStudent(studentRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/students/" + savedStudent.getId()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        assertFalse("Entity not deleted", studentRepository.findById(savedStudent.getId()).isPresent());
    }

}
