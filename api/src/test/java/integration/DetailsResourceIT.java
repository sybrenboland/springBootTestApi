package integration;

import org.shboland.persistence.db.hibernate.bean.Student;
import org.shboland.domain.entities.JsonDetails;
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
import org.shboland.persistence.db.hibernate.bean.Details;
import org.shboland.persistence.db.repo.DetailsRepository;
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
public class DetailsResourceIT {

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private DetailsRepository detailsRepository;

    // @InjectInput

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @After
    public void tearDown() {
        // @TearDownInputTop
        detailsRepository.deleteAll();
        // @TearDownInputBottom
    }

    // @Input

    
    @Test
    public void testPutStudent_withDetailsNoStudent() throws Exception {
    
        Details details = IntegrationTestFactory.givenADetails(detailsRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/detailss/" + details.getId() + "/students/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testPutStudent_withoutDetails() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/detailss/-1/students/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testGetStudent_withDetailsWithStudent() throws Exception {
    
        Details details = IntegrationTestFactory.givenADetails(detailsRepository);
        Student student = details.getStudent();

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/detailss/" + details.getId() + "/student"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/students/" + student.getId()));
    }
    
    @Test
    public void testGetStudent_withoutDetails() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/detailss/-1/student"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testGetDetails_withDetails() throws Exception {

        Details details = IntegrationTestFactory.givenADetails(detailsRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/detailss/" + details.getId()))
                        .andReturn().getResponse();
                        
        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/detailss/" + details.getId()));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"number\":" + details.getNumber()));
        // @FieldInputAssert
    }

    @Test
    public void testGetDetails_withoutDetails() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/detailss/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testList_withoutDetailss() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/detailss"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":0"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":0"));
        assertTrue("Wrong entities returned.", response.getContentAsString().contains("\"results\":[]"));
    }

    @Test
    public void testList_withDetailss() throws Exception {
    
        Details savedDetails = IntegrationTestFactory.givenADetails(detailsRepository);
        IntegrationTestFactory.givenADetails(detailsRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.get("/detailss"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong grand total returned.", response.getContentAsString().contains("\"grandTotal\":2"));
        assertTrue("Wrong number of results returned.", response.getContentAsString().contains("\"numberOfResults\":2"));
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("detailss/" + savedDetails.getId()));
    }

        
    

    @Test
    public void testPutDetails_invalidObject() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.put("/detailss/-1", new Object()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    

    @Test
    public void testPutDetails_updateObject() throws Exception {
    
        Details savedDetails = IntegrationTestFactory.givenADetails(detailsRepository);

        JsonDetails details = IntegrationTestFactory.givenAJsonDetails();

        MockHttpServletResponse response =
                mockMvc.perform(IntegrationTestUtils.doPut("/detailss/" + savedDetails.getId(), details))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity link returned.", response.getContentAsString().contains("/detailss/"));
        assertTrue("Wrong field returned.", response.getContentAsString().contains("\"number\":" + details.getNumber()));
        // @FieldInputAssert
    }

    @Test
    public void testDeleteDetails_unknownObject() throws Exception {
    
        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/detailss/-1"))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteDetails_deleteObject() throws Exception {
    
        Details savedDetails = IntegrationTestFactory.givenADetails(detailsRepository);

        MockHttpServletResponse response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/detailss/" + savedDetails.getId()))
                        .andReturn().getResponse();

        assertEquals("Wrong status code returned.", HttpStatus.OK.value(), response.getStatus());
        assertTrue("Wrong entity returned.", response.getContentAsString().isEmpty());
        assertFalse("Entity not deleted", detailsRepository.findById(savedDetails.getId()).isPresent());
    }

}
