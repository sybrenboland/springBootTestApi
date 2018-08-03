package org.shboland.api.convert;

import org.shboland.domain.entities.JsonCourseSearchCriteria;
import org.shboland.persistence.criteria.CourseSearchCriteria;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("courseSearchCriteriaConverter")
public class CourseSearchCriteriaConverter {

    public CourseSearchCriteria createSearchCriteria(JsonCourseSearchCriteria jsonCourseSearchCriteria) {
        CourseSearchCriteria.CourseSearchCriteriaBuilder searchCriteriaBuilder = CourseSearchCriteria.builder();

        searchCriteriaBuilder.start(jsonCourseSearchCriteria.getStart());
        if (jsonCourseSearchCriteria.getMaxResults() > 0) {
            searchCriteriaBuilder.maxResults(jsonCourseSearchCriteria.getMaxResults());
        } else {
            throw new ConvertException("Maximum number of results should be a positive number.");
        }

        Long id = jsonCourseSearchCriteria.getId();
        searchCriteriaBuilder.id(Optional.ofNullable(id));
        
        Boolean master = jsonCourseSearchCriteria.getMaster();
        searchCriteriaBuilder.master(Optional.ofNullable(master));
    
        // @Input

        return searchCriteriaBuilder.build();
    }
    
    // @Function input
}