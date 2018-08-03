package org.shboland.domain.entities;

import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.ResourceSupport;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonCourse extends ResourceSupport {

    @JsonProperty("master")
    private Boolean master;

    // @Input
    
}