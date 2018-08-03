package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Result;
import org.shboland.persistence.criteria.ResultSearchCriteria;

import java.util.List;

public interface ResultRepositoryCustom {

    int findNumberOfResultBySearchCriteria(ResultSearchCriteria sc);

    List<Result> findBySearchCriteria(ResultSearchCriteria sc);
}
