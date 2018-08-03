package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Student;
import org.shboland.persistence.criteria.StudentSearchCriteria;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class StudentRepositoryImpl extends AbstractHibernateRepository<Student> implements StudentRepositoryCustom {

    private static final String ID_PROPERTY = "id";
    private static final String NAME_PROPERTY = "name";
    // @Property input

    @Override
    protected Class<Student> getDomainClass() {
        return Student.class;
    }

    @Override
    public int findNumberOfStudentBySearchCriteria(StudentSearchCriteria sc) {
        CriteriaBuilder criteria = getDefaultCriteria();
        CriteriaQuery<Long> criteriaQuery = criteria.createQuery(Long.class);
        Root<Student> root = criteriaQuery.from(getDomainClass());

        List<Predicate> predicates = createPredicates(sc, criteria, root);

        criteriaQuery.select(criteria.count(root)).distinct(true)
                .where(predicates.toArray(new Predicate[predicates.size()]));

        return getEntityManager()
                .createQuery(criteriaQuery)
                .getSingleResult()
                .intValue();
    }

    @Override
    public List<Student> findBySearchCriteria(StudentSearchCriteria sc) {
        CriteriaBuilder criteria = getDefaultCriteria();
        CriteriaQuery<Student> criteriaQuery = criteria.createQuery(getDomainClass());
        Root<Student> root = criteriaQuery.from(getDomainClass());

        List<Predicate> predicates = createPredicates(sc, criteria, root);

        criteriaQuery.select(root).distinct(true)
                .where(predicates.toArray(new Predicate[predicates.size()]));

        return getEntityManager()
                .createQuery(criteriaQuery)
                .setFirstResult(sc.getStart())
                .setMaxResults(sc.getMaxResults())
                .getResultList();
    }

    private List<Predicate> createPredicates(StudentSearchCriteria sc, CriteriaBuilder criteria, Root<Student> root) {

        List<Predicate> predicates = new ArrayList<>();

        sc.getId().ifPresent(id -> predicates.add(criteria.equal(root.get(ID_PROPERTY), id)));
        
        sc.getName().ifPresent(name -> predicates.add(criteria.equal(root.get(NAME_PROPERTY), name)));
    
        // @Predicate input

        return predicates;
    }
}
