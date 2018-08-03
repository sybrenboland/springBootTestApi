package org.shboland.core.service;

import org.shboland.persistence.db.hibernate.bean.Student;
import java.util.List;
import org.shboland.persistence.criteria.DetailsSearchCriteria;
import java.util.Optional;
import org.shboland.persistence.db.hibernate.bean.Details;
import org.shboland.persistence.db.repo.DetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class DetailsService {

    private final DetailsRepository detailsRepository;
    // @FieldInput

    @Autowired
    public DetailsService(DetailsRepository detailsRepository) {
        this.detailsRepository = detailsRepository;
        // @ConstructorInput
    }
    
    // @Input

    public Student updateDetailsWithStudent(long detailsId, Student student) {
        Optional<Details> details = detailsRepository.findById(detailsId);
        if (details.isPresent()) {

            Details newDetails = details.get().toBuilder()
                    .student(student)
                    .build();
            detailsRepository.save(newDetails);

            return student;
        }

        return null;
    }
  
    public int findNumberOfDetails(DetailsSearchCriteria sc) {
        return detailsRepository.findNumberOfDetailsBySearchCriteria(sc);
    }
    

    public List<Details> findBySearchCriteria(DetailsSearchCriteria sc) {
        return detailsRepository.findBySearchCriteria(sc);
    }

    public Details save(Details details) {
        return detailsRepository.save(details);
    }

    public Optional<Details> fetchDetails(long detailsId) {
        return detailsRepository.findById(detailsId);
    }

    public boolean deleteDetails(long detailsId) {
        Optional<Details> details = detailsRepository.findById(detailsId);

        if (details.isPresent()) {
            detailsRepository.delete(details.get());
            return true;
        } else {
            return false;
        }
    }
    
}