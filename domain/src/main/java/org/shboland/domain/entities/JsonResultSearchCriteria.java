package org.shboland.domain.entities;

import javax.ws.rs.QueryParam;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonResultSearchCriteria {

    private static final int DEFAULT_MAX_RESULTS = 10;

    @QueryParam("maxResults")
    private int maxResults = DEFAULT_MAX_RESULTS;

    @QueryParam("start")
    private int start;

    @QueryParam("id")
    private Long id;
    
    @QueryParam("grade")
    private Integer grade;
    
    // @Input

}