package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Course;
import org.shboland.persistence.criteria.CourseSearchCriteria;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CourseRepositoryImpl extends AbstractHibernateRepository<Course> implements CourseRepositoryCustom {

    private static final String ID_PROPERTY = "id";
    private static final String MASTER_PROPERTY = "master";
    // @Property input

    @Override
    protected Class<Course> getDomainClass() {
        return Course.class;
    }

    @Override
    public int findNumberOfCourseBySearchCriteria(CourseSearchCriteria sc) {
        CriteriaBuilder criteria = getDefaultCriteria();
        CriteriaQuery<Long> criteriaQuery = criteria.createQuery(Long.class);
        Root<Course> root = criteriaQuery.from(getDomainClass());

        List<Predicate> predicates = createPredicates(sc, criteria, root);

        criteriaQuery.select(criteria.count(root)).distinct(true)
                .where(predicates.toArray(new Predicate[predicates.size()]));

        return getEntityManager()
                .createQuery(criteriaQuery)
                .getSingleResult()
                .intValue();
    }

    @Override
    public List<Course> findBySearchCriteria(CourseSearchCriteria sc) {
        CriteriaBuilder criteria = getDefaultCriteria();
        CriteriaQuery<Course> criteriaQuery = criteria.createQuery(getDomainClass());
        Root<Course> root = criteriaQuery.from(getDomainClass());

        List<Predicate> predicates = createPredicates(sc, criteria, root);

        criteriaQuery.select(root).distinct(true)
                .where(predicates.toArray(new Predicate[predicates.size()]));

        return getEntityManager()
                .createQuery(criteriaQuery)
                .setFirstResult(sc.getStart())
                .setMaxResults(sc.getMaxResults())
                .getResultList();
    }

    private List<Predicate> createPredicates(CourseSearchCriteria sc, CriteriaBuilder criteria, Root<Course> root) {

        List<Predicate> predicates = new ArrayList<>();

        sc.getId().ifPresent(id -> predicates.add(criteria.equal(root.get(ID_PROPERTY), id)));
        
        sc.getMaster().ifPresent(master -> predicates.add(criteria.equal(root.get(MASTER_PROPERTY), master)));
    
        // @Predicate input

        return predicates;
    }
}
