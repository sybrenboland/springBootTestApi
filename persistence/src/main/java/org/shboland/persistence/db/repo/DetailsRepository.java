package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Details;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailsRepository extends JpaRepository<Details, Long>, DetailsRepositoryCustom {
}