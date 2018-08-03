package org.shboland.api.resource;

import org.shboland.api.convert.ResultConverter;
import org.shboland.core.service.ResultService;
import org.shboland.domain.entities.JsonResult;
import org.shboland.persistence.db.hibernate.bean.Result;
import org.shboland.api.convert.DetailsConverter;
import org.shboland.core.service.DetailsService;
import org.shboland.persistence.db.hibernate.bean.Details;
import org.shboland.persistence.db.hibernate.bean.Student;
import java.util.stream.Collectors;
import javax.ws.rs.BeanParam;
import java.util.List;
import java.util.ArrayList;
import org.shboland.persistence.criteria.StudentSearchCriteria;
import org.shboland.api.convert.ConvertException;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.domain.entities.JsonStudentSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.shboland.api.convert.StudentSearchCriteriaConverter;
import org.springframework.http.HttpStatus;
import org.shboland.domain.entities.JsonStudent;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.shboland.core.service.StudentService;
import org.shboland.api.convert.StudentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class StudentController implements IStudentController {

    private final StudentService studentService;
    private final StudentConverter studentConverter;
    private final StudentSearchCriteriaConverter studentSearchCriteriaConverter;
    private final DetailsService detailsService;
    private final DetailsConverter detailsConverter;
    private final ResultService resultService;
    private final ResultConverter resultConverter;
    // @FieldInput

    @Autowired
    public StudentController(ResultConverter resultConverter, ResultService resultService, DetailsConverter detailsConverter, DetailsService detailsService, StudentSearchCriteriaConverter studentSearchCriteriaConverter, StudentService studentService, StudentConverter studentConverter) {
        this.studentService = studentService;
        this.studentConverter = studentConverter;
        this.studentSearchCriteriaConverter = studentSearchCriteriaConverter;
        this.detailsService = detailsService;
        this.detailsConverter = detailsConverter;
        this.resultService = resultService;
        this.resultConverter = resultConverter;
        // @ConstructorInput
    }
    
    // @Input

    @Override
    public ResponseEntity getResults(@PathVariable long studentId) {
        List<Result> resultList = resultService.fetchResultsForStudent(studentId);

        JsonSearchResult<JsonResult> result = JsonSearchResult.<JsonResult>builder()
                .results(resultList.stream().map(resultConverter::toJson).collect(Collectors.toList()))
                .numberOfResults(resultList.size())
                .grandTotalNumberOfResults(resultList.size())
                .build();
        
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity deleteResultWithStudent(@PathVariable long studentId, @PathVariable long resultId) {

        return studentService.removeResult(studentId, resultId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity putResultWithStudent(@PathVariable long studentId, @PathVariable long resultId) {

        return studentService.updateStudentWithResult(studentId, resultId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity deleteDetailsWithStudent(@PathVariable long studentId) {
        // Use only with @MapsId mapping
        long detailsId = studentId;

        return detailsService.deleteDetails(detailsId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity putDetailsWithStudent(@PathVariable long studentId, @PathVariable long detailsId) {

        return studentService.updateStudentWithDetails(studentId, detailsId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity getDetails(@PathVariable long studentId) {
        // Use only with @MapsId mapping
        Optional<Details> detailsOptional = detailsService.fetchDetails(studentId);

        return detailsOptional.isPresent() ? 
                ResponseEntity.ok(detailsConverter.toJson(detailsOptional.get())) : 
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<JsonStudent> getStudent(@PathVariable long studentId) {
        Optional<Student> studentOptional = studentService.fetchStudent(studentId);

        return studentOptional.isPresent() ?
                ResponseEntity.ok(studentConverter.toJson(studentOptional.get())) :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<JsonSearchResult> list(@BeanParam JsonStudentSearchCriteria searchCriteria) {

        StudentSearchCriteria sc;
        try {
            sc = studentSearchCriteriaConverter.createSearchCriteria(searchCriteria);
        } catch (ConvertException e) {
            log.warn("Conversion failed!", e);
            return ResponseEntity.badRequest().build();
        }

        List<Student> studentList = new ArrayList<>();
        int numberOfStudent = studentService.findNumberOfStudent(sc);
        if (numberOfStudent > 0) {
            studentList = studentService.findBySearchCriteria(sc);
        }

        JsonSearchResult<JsonStudent> result = JsonSearchResult.<JsonStudent>builder()
                .results(studentList.stream().map(studentConverter::toJson).collect(Collectors.toList()))
                .numberOfResults(studentList.size())
                .grandTotalNumberOfResults(numberOfStudent)
                .build();

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity postStudent(@RequestBody JsonStudent jsonStudent) {
            
        Student newStudent = studentService.save(studentConverter.fromJson(jsonStudent));

        return ResponseEntity.status(HttpStatus.CREATED).body(studentConverter.toJson(newStudent));
    }

    @Override
    public ResponseEntity<JsonStudent> putStudent(@PathVariable long studentId, @RequestBody JsonStudent jsonStudent) {

        Optional<Student> studentOptional = studentService.fetchStudent(studentId);

        Student savedStudent;
        if (!studentOptional.isPresent()) {
            savedStudent = studentService.save(studentConverter.fromJson(jsonStudent));
        } else {
            savedStudent = studentService.save(studentConverter.fromJson(jsonStudent, studentId));
        }

        return ResponseEntity.ok(studentConverter.toJson(savedStudent));
    }

    @Override
    public ResponseEntity deleteStudent(@PathVariable long studentId) {

        return studentService.deleteStudent(studentId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
    
}