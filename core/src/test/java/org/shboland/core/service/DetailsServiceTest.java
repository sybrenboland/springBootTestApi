package org.shboland.core.service;

import static org.mockito.Mockito.any;
import static org.junit.Assert.assertNull;
import org.shboland.persistence.db.hibernate.bean.Student;
import java.util.Collections;
import java.util.List;
import org.shboland.persistence.criteria.DetailsSearchCriteria;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import java.util.Optional;
import org.shboland.persistence.db.hibernate.bean.Details;
import org.shboland.persistence.db.repo.DetailsRepository;
import org.mockito.Mock;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DetailsServiceTest {
    
    @InjectMocks
    private DetailsService detailsService;
    
    @Mock
    private DetailsRepository detailsRepository;

    // @MockInput
        
    private Student student = Student.builder().build();
        
    private static final int NUMBER_OF_DETAILSS = 2;
        
    private DetailsSearchCriteria detailsSearchCriteria = DetailsSearchCriteria.builder().build();
        
    private Details details = Details.builder().build();
        
    private static final long DETAILS_ID = 3L;
    
    // @Input
        
    @Test
    public void testUpdateDetailsWithStudent_NoDetailsFound() {

        when(detailsRepository.findById(DETAILS_ID)).thenReturn(Optional.empty());

        Student resultStudent = detailsService.updateDetailsWithStudent(DETAILS_ID, student);

        assertNull("Wrong result returned!", resultStudent);
        verify(detailsRepository, never()).save(any(Details.class));
    }

    @Test
    public void testUpdateDetailsWithStudent() {

        when(detailsRepository.findById(DETAILS_ID)).thenReturn(Optional.of(details));

        Student resultStudent = detailsService.updateDetailsWithStudent(DETAILS_ID, student);

        assertNotNull("Wrong result returned!", resultStudent);
        assertEquals("Wrong object returned!", student, resultStudent);
        verify(detailsRepository, times(1)).save(any(Details.class));
    }
    
    @Test
    public void testFindNumberOfDetails() {

        when(detailsRepository.findNumberOfDetailsBySearchCriteria(detailsSearchCriteria)).thenReturn(NUMBER_OF_DETAILSS);

        int resultNumber = detailsService.findNumberOfDetails(detailsSearchCriteria);

        assertEquals("Wrong number returned!", NUMBER_OF_DETAILSS, resultNumber);
    } 
 
    @Test
    public void testFindBySearchCriteria() {

        when(detailsRepository.findBySearchCriteria(detailsSearchCriteria)).thenReturn(Collections.singletonList(details));

        List<Details> resultDetailsList = detailsService.findBySearchCriteria(detailsSearchCriteria);

        assertNotNull("No object returned!", resultDetailsList);
        assertEquals("Wrong number of objects returned!", 1, resultDetailsList.size());
        assertEquals("Wrong object returned!", details, resultDetailsList.get(0));
    }

    @Test
    public void testSaveDetails() {

        when(detailsRepository.save(details)).thenReturn(details);

        Details savedDetails = detailsService.save(details);

        assertNotNull("Wrong result returned!", savedDetails);
        assertEquals("Wrong object returned!", details, savedDetails);
    }

    @Test
    public void testFetchDetails() {

        when(detailsRepository.findById(DETAILS_ID)).thenReturn(Optional.of(details));

        Optional<Details> fetchResult = detailsService.fetchDetails(DETAILS_ID);

        assertTrue("Wrong result returned!", fetchResult.isPresent());
        assertEquals("Wrong object returned!", details, fetchResult.get());
    }
    
    @Test
    public void testDeleteDetails() {

        when(detailsRepository.findById(DETAILS_ID)).thenReturn(Optional.of(details));

        boolean resultDelete = detailsService.deleteDetails(DETAILS_ID);

        assertTrue("Wrong result returned!", resultDelete);
        verify(detailsRepository, times(1)).delete(details);
    }

    @Test
    public void testDeleteDetails_NoDetailsFound() {

        when(detailsRepository.findById(DETAILS_ID)).thenReturn(Optional.empty());

        boolean resultDelete = detailsService.deleteDetails(DETAILS_ID);

        assertFalse("Wrong result returned!", resultDelete);
        verify(detailsRepository, never()).delete(details);
    }
}