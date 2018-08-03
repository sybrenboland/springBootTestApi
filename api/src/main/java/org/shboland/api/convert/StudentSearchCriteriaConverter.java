package org.shboland.api.convert;

import org.shboland.domain.entities.JsonStudentSearchCriteria;
import org.shboland.persistence.criteria.StudentSearchCriteria;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("studentSearchCriteriaConverter")
public class StudentSearchCriteriaConverter {

    public StudentSearchCriteria createSearchCriteria(JsonStudentSearchCriteria jsonStudentSearchCriteria) {
        StudentSearchCriteria.StudentSearchCriteriaBuilder searchCriteriaBuilder = StudentSearchCriteria.builder();

        searchCriteriaBuilder.start(jsonStudentSearchCriteria.getStart());
        if (jsonStudentSearchCriteria.getMaxResults() > 0) {
            searchCriteriaBuilder.maxResults(jsonStudentSearchCriteria.getMaxResults());
        } else {
            throw new ConvertException("Maximum number of results should be a positive number.");
        }

        Long id = jsonStudentSearchCriteria.getId();
        searchCriteriaBuilder.id(Optional.ofNullable(id));
        
        String name = jsonStudentSearchCriteria.getName();
        searchCriteriaBuilder.name(Optional.ofNullable(name));
    
        // @Input

        return searchCriteriaBuilder.build();
    }
    
    // @Function input
}