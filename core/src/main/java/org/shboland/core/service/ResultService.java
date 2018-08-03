package org.shboland.core.service;

import org.shboland.persistence.db.repo.StudentRepository;
import org.shboland.persistence.db.hibernate.bean.Student;
import java.util.List;
import org.shboland.persistence.criteria.ResultSearchCriteria;
import java.util.Optional;
import org.shboland.persistence.db.hibernate.bean.Result;
import org.shboland.persistence.db.repo.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ResultService {

    private final ResultRepository resultRepository;
    private final StudentRepository studentRepository;
    // @FieldInput

    @Autowired
    public ResultService(StudentRepository studentRepository, ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
        this.studentRepository = studentRepository;
        // @ConstructorInput
    }
    
    // @Input

    public List<Result> fetchResultsForStudent(long studentId) {
        ResultSearchCriteria resultSearchCriteria =  ResultSearchCriteria.builder()
                .studentId(Optional.of(studentId))
                .build();

        return resultRepository.findBySearchCriteria(resultSearchCriteria);
    }

    public boolean removeStudent(long resultId, long studentId) {
        Optional<Result> resultOptional = resultRepository.findById(resultId);
        if (resultOptional.isPresent()) {
            Result result = resultOptional.get();
         
            if (result.getStudent() != null) {

                Optional<Student> studentOptional = studentRepository.findById(studentId);
                if (studentOptional.isPresent() && studentOptional.get().getId().equals(result.getStudent().getId())) {
    
                    Result newResult = result.toBuilder()
                            .student(null)
                            .build();
                    resultRepository.save(newResult);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean updateResultWithStudent(long resultId, long studentId) {
        Optional<Result> resultOptional = resultRepository.findById(resultId);
        if (resultOptional.isPresent()) {

            Optional<Student> studentOptional = studentRepository.findById(studentId);
            if (studentOptional.isPresent()) {

                Result newResult = resultOptional.get().toBuilder()
                        .student(studentOptional.get())
                        .build();
                resultRepository.save(newResult);
                return true;
            }
        }

        return false;
    }
  
    public int findNumberOfResult(ResultSearchCriteria sc) {
        return resultRepository.findNumberOfResultBySearchCriteria(sc);
    }
    

    public List<Result> findBySearchCriteria(ResultSearchCriteria sc) {
        return resultRepository.findBySearchCriteria(sc);
    }

    public Result save(Result result) {
        return resultRepository.save(result);
    }

    public Optional<Result> fetchResult(long resultId) {
        return resultRepository.findById(resultId);
    }

    public boolean deleteResult(long resultId) {
        Optional<Result> result = resultRepository.findById(resultId);

        if (result.isPresent()) {
            resultRepository.delete(result.get());
            return true;
        } else {
            return false;
        }
    }
    
}