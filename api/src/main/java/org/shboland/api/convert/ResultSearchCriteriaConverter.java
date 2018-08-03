package org.shboland.api.convert;

import org.shboland.domain.entities.JsonResultSearchCriteria;
import org.shboland.persistence.criteria.ResultSearchCriteria;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("resultSearchCriteriaConverter")
public class ResultSearchCriteriaConverter {

    public ResultSearchCriteria createSearchCriteria(JsonResultSearchCriteria jsonResultSearchCriteria) {
        ResultSearchCriteria.ResultSearchCriteriaBuilder searchCriteriaBuilder = ResultSearchCriteria.builder();

        searchCriteriaBuilder.start(jsonResultSearchCriteria.getStart());
        if (jsonResultSearchCriteria.getMaxResults() > 0) {
            searchCriteriaBuilder.maxResults(jsonResultSearchCriteria.getMaxResults());
        } else {
            throw new ConvertException("Maximum number of results should be a positive number.");
        }

        Long id = jsonResultSearchCriteria.getId();
        searchCriteriaBuilder.id(Optional.ofNullable(id));
        
        Integer grade = jsonResultSearchCriteria.getGrade();
        searchCriteriaBuilder.grade(Optional.ofNullable(grade));
    
        // @Input

        return searchCriteriaBuilder.build();
    }
    
    // @Function input
}