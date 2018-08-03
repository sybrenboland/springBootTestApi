package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Student;
import org.shboland.persistence.criteria.StudentSearchCriteria;

import java.util.List;

public interface StudentRepositoryCustom {

    int findNumberOfStudentBySearchCriteria(StudentSearchCriteria sc);

    List<Student> findBySearchCriteria(StudentSearchCriteria sc);
}
