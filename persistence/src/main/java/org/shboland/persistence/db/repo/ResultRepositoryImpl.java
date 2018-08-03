package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Result;
import org.shboland.persistence.criteria.ResultSearchCriteria;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ResultRepositoryImpl extends AbstractHibernateRepository<Result> implements ResultRepositoryCustom {

    private static final String ID_PROPERTY = "id";
    private static final String GRADE_PROPERTY = "grade";
    private static final String STUDENT_PROPERTY = "student";
    // @Property input

    @Override
    protected Class<Result> getDomainClass() {
        return Result.class;
    }

    @Override
    public int findNumberOfResultBySearchCriteria(ResultSearchCriteria sc) {
        CriteriaBuilder criteria = getDefaultCriteria();
        CriteriaQuery<Long> criteriaQuery = criteria.createQuery(Long.class);
        Root<Result> root = criteriaQuery.from(getDomainClass());

        List<Predicate> predicates = createPredicates(sc, criteria, root);

        criteriaQuery.select(criteria.count(root)).distinct(true)
                .where(predicates.toArray(new Predicate[predicates.size()]));

        return getEntityManager()
                .createQuery(criteriaQuery)
                .getSingleResult()
                .intValue();
    }

    @Override
    public List<Result> findBySearchCriteria(ResultSearchCriteria sc) {
        CriteriaBuilder criteria = getDefaultCriteria();
        CriteriaQuery<Result> criteriaQuery = criteria.createQuery(getDomainClass());
        Root<Result> root = criteriaQuery.from(getDomainClass());

        List<Predicate> predicates = createPredicates(sc, criteria, root);

        criteriaQuery.select(root).distinct(true)
                .where(predicates.toArray(new Predicate[predicates.size()]));

        return getEntityManager()
                .createQuery(criteriaQuery)
                .setFirstResult(sc.getStart())
                .setMaxResults(sc.getMaxResults())
                .getResultList();
    }

    private List<Predicate> createPredicates(ResultSearchCriteria sc, CriteriaBuilder criteria, Root<Result> root) {

        List<Predicate> predicates = new ArrayList<>();

        sc.getId().ifPresent(id -> predicates.add(criteria.equal(root.get(ID_PROPERTY), id)));
        
        sc.getGrade().ifPresent(grade -> predicates.add(criteria.equal(root.get(GRADE_PROPERTY), grade)));
    
        sc.getStudentId().ifPresent(studentId -> predicates.add(criteria.equal(root.get(STUDENT_PROPERTY).get(ID_PROPERTY), studentId)));
    
        // @Predicate input

        return predicates;
    }
}
