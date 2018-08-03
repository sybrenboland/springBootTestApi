package org.shboland.api.resource;

import org.shboland.api.convert.StudentConverter;
import org.shboland.core.service.StudentService;
import org.shboland.persistence.db.hibernate.bean.Student;
import org.shboland.persistence.db.hibernate.bean.Result;
import java.util.stream.Collectors;
import javax.ws.rs.BeanParam;
import java.util.List;
import java.util.ArrayList;
import org.shboland.persistence.criteria.ResultSearchCriteria;
import org.shboland.api.convert.ConvertException;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.domain.entities.JsonResultSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.shboland.api.convert.ResultSearchCriteriaConverter;
import org.springframework.http.HttpStatus;
import org.shboland.domain.entities.JsonResult;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.shboland.core.service.ResultService;
import org.shboland.api.convert.ResultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ResultController implements IResultController {

    private final ResultService resultService;
    private final ResultConverter resultConverter;
    private final ResultSearchCriteriaConverter resultSearchCriteriaConverter;
    private final StudentService studentService;
    private final StudentConverter studentConverter;
    // @FieldInput

    @Autowired
    public ResultController(StudentConverter studentConverter, StudentService studentService, ResultSearchCriteriaConverter resultSearchCriteriaConverter, ResultService resultService, ResultConverter resultConverter) {
        this.resultService = resultService;
        this.resultConverter = resultConverter;
        this.resultSearchCriteriaConverter = resultSearchCriteriaConverter;
        this.studentService = studentService;
        this.studentConverter = studentConverter;
        // @ConstructorInput
    }
    
    // @Input

    @Override
    public ResponseEntity getStudent(@PathVariable long resultId) {
        Optional<Student> studentOptional = studentService.fetchStudentForResult(resultId);

        return studentOptional.isPresent() ? 
                 ResponseEntity.ok(studentConverter.toJson(studentOptional.get())) : 
                 ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity deleteStudentWithResult(@PathVariable long resultId, @PathVariable long studentId) {

        return resultService.removeStudent(resultId, studentId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity putStudentWithResult(@PathVariable long resultId, @PathVariable long studentId) {

        return resultService.updateResultWithStudent(resultId, studentId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<JsonResult> getResult(@PathVariable long resultId) {
        Optional<Result> resultOptional = resultService.fetchResult(resultId);

        return resultOptional.isPresent() ?
                ResponseEntity.ok(resultConverter.toJson(resultOptional.get())) :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<JsonSearchResult> list(@BeanParam JsonResultSearchCriteria searchCriteria) {

        ResultSearchCriteria sc;
        try {
            sc = resultSearchCriteriaConverter.createSearchCriteria(searchCriteria);
        } catch (ConvertException e) {
            log.warn("Conversion failed!", e);
            return ResponseEntity.badRequest().build();
        }

        List<Result> resultList = new ArrayList<>();
        int numberOfResult = resultService.findNumberOfResult(sc);
        if (numberOfResult > 0) {
            resultList = resultService.findBySearchCriteria(sc);
        }

        JsonSearchResult<JsonResult> result = JsonSearchResult.<JsonResult>builder()
                .results(resultList.stream().map(resultConverter::toJson).collect(Collectors.toList()))
                .numberOfResults(resultList.size())
                .grandTotalNumberOfResults(numberOfResult)
                .build();

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity postResult(@RequestBody JsonResult jsonResult) {
            
        Result newResult = resultService.save(resultConverter.fromJson(jsonResult));

        return ResponseEntity.status(HttpStatus.CREATED).body(resultConverter.toJson(newResult));
    }

    @Override
    public ResponseEntity<JsonResult> putResult(@PathVariable long resultId, @RequestBody JsonResult jsonResult) {

        Optional<Result> resultOptional = resultService.fetchResult(resultId);

        Result savedResult;
        if (!resultOptional.isPresent()) {
            savedResult = resultService.save(resultConverter.fromJson(jsonResult));
        } else {
            savedResult = resultService.save(resultConverter.fromJson(jsonResult, resultId));
        }

        return ResponseEntity.ok(resultConverter.toJson(savedResult));
    }

    @Override
    public ResponseEntity deleteResult(@PathVariable long resultId) {

        return resultService.deleteResult(resultId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
    
}