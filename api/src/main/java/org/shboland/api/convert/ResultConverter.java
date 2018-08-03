package org.shboland.api.convert;

import org.shboland.persistence.db.hibernate.bean.Result;
import org.shboland.domain.entities.JsonResult;
import org.shboland.api.resource.ResultController;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Service
public class ResultConverter {
    
    public JsonResult toJson(Result result) {
        JsonResult jsonResult = JsonResult.builder()
                .grade(result.getGrade())
                // @InputJsonField
                .build();
        
        jsonResult.add(linkTo(ResultController.class).slash(result.getId()).withSelfRel());
        jsonResult.add(linkTo(ResultController.class).slash(result.getId()).slash("/students").withRel("student"));
            // @InputLink

        return jsonResult;
    }
    
    public Result fromJson(JsonResult jsonResult) {
        return resultBuilder(jsonResult).build();
    }

    public Result fromJson(JsonResult jsonResult, long resultId) {
        return resultBuilder(jsonResult)
                .id(resultId)
                .build();
    }

    private Result.ResultBuilder resultBuilder(JsonResult jsonResult) {

        return Result.builder()
                .grade(jsonResult.getGrade())
                // @InputBeanField
        ;
    }
}