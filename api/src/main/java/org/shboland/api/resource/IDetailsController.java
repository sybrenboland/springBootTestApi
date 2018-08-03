package org.shboland.api.resource;

import org.shboland.domain.entities.JsonStudent;
import javax.ws.rs.BeanParam;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.domain.entities.JsonDetailsSearchCriteria;
import org.shboland.domain.entities.JsonDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/detailss")
public interface IDetailsController {

    // @Input

    @RequestMapping(value = "/{detailsId}/student", method = RequestMethod.PUT)
    ResponseEntity putStudentWithDetails(@PathVariable("detailsId") long detailsId, @RequestBody JsonStudent student);

    @RequestMapping(path = "/{detailsId}/student", method = RequestMethod.GET)
    ResponseEntity getStudent(@PathVariable long detailsId);

    @RequestMapping(path = "/{detailsId}", method = RequestMethod.GET)
    ResponseEntity<JsonDetails> getDetails(@PathVariable long detailsId);
    
    @RequestMapping(path = "", method = RequestMethod.GET)
    ResponseEntity<JsonSearchResult> list(@BeanParam JsonDetailsSearchCriteria searchCriteria);
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    ResponseEntity postDetails(@RequestBody JsonDetails details);
    
    @RequestMapping(value = "/{detailsId}", method = RequestMethod.PUT)
    ResponseEntity putDetails(@PathVariable("detailsId") long detailsId, @RequestBody JsonDetails jsonDetails);
    
    @RequestMapping(value = "/{detailsId}", method = RequestMethod.DELETE)
    ResponseEntity deleteDetails(@PathVariable("detailsId") long detailsId);
    
}