package integration;

import org.shboland.persistence.db.repo.StudentRepository;
import org.shboland.persistence.db.hibernate.bean.Student;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.shboland.domain.entities.JsonResult;
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
import org.shboland.persistence.db.hibernate.bean.Result;
import org.shboland.persistence.db.repo.ResultRepository;
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
public class ResultResourceIT {

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ResultRepository resultRepository;

    
    @Autowired
    private StudentRepository studentRepository;

     // @InjectInput

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() {
        // @TearDownInputTop
        resultRepository.deleteAll();
        
        studentRepository.deleteAll();
     // @TearDownInputBottom
    }

    // @Input

    @Test
    public void testGetStudent_withResultWithStudent() throws Exception {
    
        Result result = IntegrationTestFactory.givenAResultWithStudent(resultRepository, studentRepository);
        Student student = result.getStudent();

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/results/" + result.getId() + "/students"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/students/" + student.getId()));
    }

    @Test
    public void testGetStudent_withResultNoStudent() throws Exception {
    
        Result result = IntegrationTestFactory.givenAResult(resultRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/results/" + result.getId() + "/students"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testGetStudent_withoutResult() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/results/-1/students"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteStudent_withResultWithStudents() throws Exception {
    
        Result result = IntegrationTestFactory.givenAResultWithStudent(resultRepository, studentRepository);
        Student student = result.getStudent();

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/results/" + result.getId() + "/students/" + student.getId()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        assertFalse("Wrong entity link returned.",
                mockMvc.perform(MockMvcRequestBuilders.get("/results/" + result.getId() + "/students"))
                        .andReturn().getResponse().getContentAsString()
                        .contains("/students/" + student.getId()));
    }

    @Test
    public void testDeleteStudent_withResultNoStudents() throws Exception {
    
        Result result = IntegrationTestFactory.givenAResult(resultRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/results/" + result.getId() + "/students/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteStudent_withoutResult() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("results/-1/students/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        

        mockMvc.perform(MockMvcRequestBuilders.delete("/results/-1/students/-1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    
    @Test
    public void testPutStudent_withResult() throws Exception {

        Result result = IntegrationTestFactory.givenAResult(resultRepository);
        Student student = IntegrationTestFactory.givenAStudent(studentRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/results/" + result.getId() + "/students/" + student.getId()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        assertTrue("Wrong entity link returned.",
                mockMvc.perform(MockMvcRequestBuilders.get("/results/" + result.getId() + "/students"))
                        .andReturn().getResponse().getContentAsString()
                        .contains("/students/" + student.getId()));
    }
    
    @Test
    public void testPutStudent_withResultWithStudent() throws Exception {
    
        Result result = IntegrationTestFactory.givenAResultWithStudent(resultRepository, studentRepository);
        Student student = result.getStudent();

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/results/" + result.getId() + "/students/" + student.getId()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        assertTrue("Wrong entity link returned.",
                mockMvc.perform(MockMvcRequestBuilders.get("/results/" + result.getId() + "/students"))
                        .andReturn().getResponse().getContentAsString()
                        .contains("/students/" + student.getId()));
    }

    @Test
    public void testPutStudent_withResultNoStudent() throws Exception {
    
        Result result = IntegrationTestFactory.givenAResult(resultRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/results/" + result.getId() + "/students/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testPutStudent_withoutResult() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/results/-1/students/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testGetResult_withResult() throws Exception {

        Result result = IntegrationTestFactory.givenAResult(resultRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/results/" + result.getId()))
                        .andReturn().getResponse();
                        
        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/results/" + result.getId()));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"grade\":" + result.getGrade()));
        // @FieldInputAssert
    }

    @Test
    public void testGetResult_withoutResult() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/results/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testList_withoutResults() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/results"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":0"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":0"));
        assertTrue("Wrong entities returned.", response.getContentAsString().contains("\"results\":[]"));
    }

    @Test
    public void testList_withResults() throws Exception {
    
        Result savedResult = IntegrationTestFactory.givenAResult(resultRepository);
        IntegrationTestFactory.givenAResult(resultRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/results"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":2"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":2"));
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("results/" + savedResult.getId()));
    }

        
    @Test
    public void testPostResult_invalidObject() throws Exception {
    
         MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.post("/results"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testPostResult_newObject() throws Exception {
    
        JsonResult result = IntegrationTestFactory.givenAJsonResult();

        MockHttpServletResponse response =
                mockMvc.perform(IntegrationTestUtils.doPost("/results", result))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.CREATED.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/results/"));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"grade\":" + result.getGrade()));
        // @FieldInputAssert
    }

    @Test
    public void testPutResult_invalidObject() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/results/-1", new Object()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testPutResult_newObject() throws Exception {
    
        JsonResult result = IntegrationTestFactory.givenAJsonResult();

        MockHttpServletResponse response =
                mockMvc.perform(IntegrationTestUtils.doPut("/results/-1", result))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/results/"));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"grade\":" + result.getGrade()));
        // @FieldInputAssert
    }

    @Test
    public void testPutResult_updateObject() throws Exception {
    
        Result savedResult = IntegrationTestFactory.givenAResult(resultRepository);

        JsonResult result = IntegrationTestFactory.givenAJsonResult();

        MockHttpServletResponse response =
                mockMvc.perform(IntegrationTestUtils.doPut("/results/" + savedResult.getId(), result))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/results/"));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"grade\":" + result.getGrade()));
        // @FieldInputAssert
    }

    @Test
    public void testDeleteResult_unknownObject() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/results/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteResult_deleteObject() throws Exception {
    
        Result savedResult = IntegrationTestFactory.givenAResult(resultRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/results/" + savedResult.getId()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        assertFalse("Entity not deleted", resultRepository.findById(savedResult.getId()).isPresent());
    }

}
