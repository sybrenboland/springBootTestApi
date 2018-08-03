package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Course;
import org.shboland.persistence.criteria.CourseSearchCriteria;

import java.util.List;

public interface CourseRepositoryCustom {

    int findNumberOfCourseBySearchCriteria(CourseSearchCriteria sc);

    List<Course> findBySearchCriteria(CourseSearchCriteria sc);
}
