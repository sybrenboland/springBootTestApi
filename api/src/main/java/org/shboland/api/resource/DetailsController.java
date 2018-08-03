package org.shboland.api.resource;

import org.shboland.domain.entities.JsonStudent;
import org.shboland.api.convert.StudentConverter;
import org.shboland.core.service.StudentService;
import org.shboland.persistence.db.hibernate.bean.Student;
import org.shboland.persistence.db.hibernate.bean.Details;
import java.util.stream.Collectors;
import javax.ws.rs.BeanParam;
import java.util.List;
import java.util.ArrayList;
import org.shboland.persistence.criteria.DetailsSearchCriteria;
import org.shboland.api.convert.ConvertException;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.domain.entities.JsonDetailsSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.shboland.api.convert.DetailsSearchCriteriaConverter;
import org.springframework.http.HttpStatus;
import org.shboland.domain.entities.JsonDetails;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.shboland.core.service.DetailsService;
import org.shboland.api.convert.DetailsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class DetailsController implements IDetailsController {

    private final DetailsService detailsService;
    private final DetailsConverter detailsConverter;
    private final DetailsSearchCriteriaConverter detailsSearchCriteriaConverter;
    private final StudentService studentService;
    private final StudentConverter studentConverter;
    // @FieldInput

    @Autowired
    public DetailsController(StudentConverter studentConverter, StudentService studentService, DetailsSearchCriteriaConverter detailsSearchCriteriaConverter, DetailsService detailsService, DetailsConverter detailsConverter) {
        this.detailsService = detailsService;
        this.detailsConverter = detailsConverter;
        this.detailsSearchCriteriaConverter = detailsSearchCriteriaConverter;
        this.studentService = studentService;
        this.studentConverter = studentConverter;
        // @ConstructorInput
    }
    
    // @Input

    @Override
    public ResponseEntity putStudentWithDetails(@PathVariable long detailsId, @RequestBody JsonStudent jsonStudent) {

        Student newStudent = detailsService.updateDetailsWithStudent(detailsId, studentConverter.fromJson(jsonStudent));

        return  newStudent != null ?
                ResponseEntity.ok(studentConverter.toJson(newStudent)) :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity getStudent(@PathVariable long detailsId) {
        // Use only with @MapsId mapping
        Optional<Student> studentOptional = studentService.fetchStudent(detailsId);

        return studentOptional.isPresent() ? 
                ResponseEntity.ok(studentConverter.toJson(studentOptional.get())) : 
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<JsonDetails> getDetails(@PathVariable long detailsId) {
        Optional<Details> detailsOptional = detailsService.fetchDetails(detailsId);

        return detailsOptional.isPresent() ?
                ResponseEntity.ok(detailsConverter.toJson(detailsOptional.get())) :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<JsonSearchResult> list(@BeanParam JsonDetailsSearchCriteria searchCriteria) {

        DetailsSearchCriteria sc;
        try {
            sc = detailsSearchCriteriaConverter.createSearchCriteria(searchCriteria);
        } catch (ConvertException e) {
            log.warn("Conversion failed!", e);
            return ResponseEntity.badRequest().build();
        }

        List<Details> detailsList = new ArrayList<>();
        int numberOfDetails = detailsService.findNumberOfDetails(sc);
        if (numberOfDetails > 0) {
            detailsList = detailsService.findBySearchCriteria(sc);
        }

        JsonSearchResult<JsonDetails> result = JsonSearchResult.<JsonDetails>builder()
                .results(detailsList.stream().map(detailsConverter::toJson).collect(Collectors.toList()))
                .numberOfResults(detailsList.size())
                .grandTotalNumberOfResults(numberOfDetails)
                .build();

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity postDetails(@RequestBody JsonDetails jsonDetails) {
            
        Details newDetails = detailsService.save(detailsConverter.fromJson(jsonDetails));

        return ResponseEntity.status(HttpStatus.CREATED).body(detailsConverter.toJson(newDetails));
    }

    @Override
    public ResponseEntity<JsonDetails> putDetails(@PathVariable long detailsId, @RequestBody JsonDetails jsonDetails) {

        Optional<Details> detailsOptional = detailsService.fetchDetails(detailsId);

        Details savedDetails;
        if (!detailsOptional.isPresent()) {
            return ResponseEntity.badRequest().build();
        } else {
            savedDetails = detailsService.save(detailsConverter.fromJson(jsonDetails, detailsId));
        }

        return ResponseEntity.ok(detailsConverter.toJson(savedDetails));
    }

    @Override
    public ResponseEntity deleteDetails(@PathVariable long detailsId) {

        return detailsService.deleteDetails(detailsId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
    
}