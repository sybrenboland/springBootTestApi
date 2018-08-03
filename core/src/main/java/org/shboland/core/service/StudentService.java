package org.shboland.core.service;

import org.shboland.persistence.db.repo.ResultRepository;
import org.shboland.persistence.db.hibernate.bean.Result;
import org.shboland.persistence.db.repo.DetailsRepository;
import org.shboland.persistence.db.hibernate.bean.Details;
import java.util.List;
import org.shboland.persistence.criteria.StudentSearchCriteria;
import java.util.Optional;
import org.shboland.persistence.db.hibernate.bean.Student;
import org.shboland.persistence.db.repo.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class StudentService {

    private final StudentRepository studentRepository;
    private final DetailsRepository detailsRepository;
    private final ResultRepository resultRepository;
    // @FieldInput

    @Autowired
    public StudentService(ResultRepository resultRepository, DetailsRepository detailsRepository, StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.detailsRepository = detailsRepository;
        this.resultRepository = resultRepository;
        // @ConstructorInput
    }
    
    // @Input

    public Optional<Student> fetchStudentForResult(long resultId) {
        Optional<Result> resultOptional = resultRepository.findById(resultId);
        return resultOptional.isPresent() && resultOptional.get().getStudent() != null ? 
                Optional.of(resultOptional.get().getStudent()) : 
                Optional.empty();
    }

    public boolean removeResult(long studentId, long resultId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();

            Optional<Result> resultOptional = resultRepository.findById(resultId);
            if (resultOptional.isPresent()) {
                Result result = resultOptional.get();

                if (result.getStudent() != null && student.getId().equals(result.getStudent().getId())) {
    
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

    public boolean updateStudentWithResult(long studentId, long resultId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();

            Optional<Result> resultOptional = resultRepository.findById(resultId);
            if (resultOptional.isPresent()) {

                Result newResult = resultOptional.get().toBuilder()
                        .student(student)
                        .build();
                resultRepository.save(newResult);
                return true;
            }
        }

        return false;
    }

    public boolean updateStudentWithDetails(long studentId, long detailsId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isPresent()) {

            Optional<Details> detailsOptional = detailsRepository.findById(detailsId);
            if (detailsOptional.isPresent()) {

                Details newDetails = detailsOptional.get().toBuilder()
                        .student(studentOptional.get())
                        .build();
                detailsRepository.save(newDetails);
                return true;
            }
        }

        return false;
    }
  
    public int findNumberOfStudent(StudentSearchCriteria sc) {
        return studentRepository.findNumberOfStudentBySearchCriteria(sc);
    }
    

    public List<Student> findBySearchCriteria(StudentSearchCriteria sc) {
        return studentRepository.findBySearchCriteria(sc);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public Optional<Student> fetchStudent(long studentId) {
        return studentRepository.findById(studentId);
    }

    public boolean deleteStudent(long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);

        if (student.isPresent()) {
            studentRepository.delete(student.get());
            return true;
        } else {
            return false;
        }
    }
    
}