package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseRepositoryCustom {
}