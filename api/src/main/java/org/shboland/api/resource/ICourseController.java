package org.shboland.api.resource;

import javax.ws.rs.BeanParam;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.domain.entities.JsonCourseSearchCriteria;
import org.shboland.domain.entities.JsonCourse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/courses")
public interface ICourseController {

    // @Input

    @RequestMapping(path = "/{courseId}", method = RequestMethod.GET)
    ResponseEntity<JsonCourse> getCourse(@PathVariable long courseId);
    
    @RequestMapping(path = "", method = RequestMethod.GET)
    ResponseEntity<JsonSearchResult> list(@BeanParam JsonCourseSearchCriteria searchCriteria);
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    ResponseEntity postCourse(@RequestBody JsonCourse course);
    
    @RequestMapping(value = "/{courseId}", method = RequestMethod.PUT)
    ResponseEntity putCourse(@PathVariable("courseId") long courseId, @RequestBody JsonCourse jsonCourse);
    
    @RequestMapping(value = "/{courseId}", method = RequestMethod.DELETE)
    ResponseEntity deleteCourse(@PathVariable("courseId") long courseId);
    
}