package org.shboland.persistence.criteria;

import java.util.Optional;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class StudentSearchCriteria {

    @Builder.Default
    private int maxResults = 10;

    @Builder.Default
    private int start = 0;

    @Builder.Default
    private Optional<Long> id = Optional.empty();
    
    @Builder.Default
    private Optional<String> name = Optional.empty();
    
    // @Input
}
