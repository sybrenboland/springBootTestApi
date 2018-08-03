package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long>, StudentRepositoryCustom {
}