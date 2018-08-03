package org.shboland.api.resource;

import javax.ws.rs.BeanParam;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.domain.entities.JsonStudentSearchCriteria;
import org.shboland.domain.entities.JsonStudent;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/students")
public interface IStudentController {

    // @Input

    @RequestMapping(path = "/{studentId}/results", method = RequestMethod.GET)
    ResponseEntity getResults(@PathVariable long studentId);

    @RequestMapping(value = "/{studentId}/results/{resultId}", method = RequestMethod.DELETE)
    ResponseEntity deleteResultWithStudent(@PathVariable("studentId") long studentId, @PathVariable("resultId") long resultId);

    @RequestMapping(value = "/{studentId}/results/{resultId}", method = RequestMethod.PUT)
    ResponseEntity putResultWithStudent(@PathVariable("studentId") long studentId, @PathVariable("resultId") long resultId);

    @RequestMapping(value = "/{studentId}/details", method = RequestMethod.DELETE)
    ResponseEntity deleteDetailsWithStudent(@PathVariable("studentId") long studentId);

    @RequestMapping(value = "/{studentId}/detailss/{detailsId}", method = RequestMethod.PUT)
    ResponseEntity putDetailsWithStudent(@PathVariable("studentId") long studentId, @PathVariable("detailsId") long detailsId);

    @RequestMapping(path = "/{studentId}/details", method = RequestMethod.GET)
    ResponseEntity getDetails(@PathVariable long studentId);

    @RequestMapping(path = "/{studentId}", method = RequestMethod.GET)
    ResponseEntity<JsonStudent> getStudent(@PathVariable long studentId);
    
    @RequestMapping(path = "", method = RequestMethod.GET)
    ResponseEntity<JsonSearchResult> list(@BeanParam JsonStudentSearchCriteria searchCriteria);
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    ResponseEntity postStudent(@RequestBody JsonStudent student);
    
    @RequestMapping(value = "/{studentId}", method = RequestMethod.PUT)
    ResponseEntity putStudent(@PathVariable("studentId") long studentId, @RequestBody JsonStudent jsonStudent);
    
    @RequestMapping(value = "/{studentId}", method = RequestMethod.DELETE)
    ResponseEntity deleteStudent(@PathVariable("studentId") long studentId);
    
}