package org.shboland.api.resource;

import javax.ws.rs.BeanParam;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.domain.entities.JsonResultSearchCriteria;
import org.shboland.domain.entities.JsonResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/results")
public interface IResultController {

    // @Input

    @RequestMapping(path = "/{resultId}/students", method = RequestMethod.GET)
    ResponseEntity getStudent(@PathVariable long resultId);

    @RequestMapping(value = "/{resultId}/students/{studentId}", method = RequestMethod.DELETE)
    ResponseEntity deleteStudentWithResult(@PathVariable("resultId") long resultId, @PathVariable("studentId") long studentId);

    @RequestMapping(value = "/{resultId}/students/{studentId}", method = RequestMethod.PUT)
    ResponseEntity putStudentWithResult(@PathVariable("resultId") long resultId, @PathVariable("studentId") long studentId);

    @RequestMapping(path = "/{resultId}", method = RequestMethod.GET)
    ResponseEntity<JsonResult> getResult(@PathVariable long resultId);
    
    @RequestMapping(path = "", method = RequestMethod.GET)
    ResponseEntity<JsonSearchResult> list(@BeanParam JsonResultSearchCriteria searchCriteria);
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    ResponseEntity postResult(@RequestBody JsonResult result);
    
    @RequestMapping(value = "/{resultId}", method = RequestMethod.PUT)
    ResponseEntity putResult(@PathVariable("resultId") long resultId, @RequestBody JsonResult jsonResult);
    
    @RequestMapping(value = "/{resultId}", method = RequestMethod.DELETE)
    ResponseEntity deleteResult(@PathVariable("resultId") long resultId);
    
}