package com.example.model.specification;

import com.example.controller.dto.UserFilterRequest;
import com.example.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

import static org.springframework.data.jpa.domain.Specification.where;

public class UserSpecification {
    private static final String NAME = "name";
    private static final String CREATED_AT = "createdAt";

    private static Specification<User> hasName(final String name) {
        if (name == null)
            return null;

        return (root, query, builder) ->
                builder.like(builder.lower(root.get(NAME)), "%" + name.toLowerCase() + "%");
    }

    private static Specification<User> hasCreatedAt(final LocalDateTime startTime, final LocalDateTime endTime) {
        if (startTime == null && endTime == null)
            return null;

        if (startTime == null)
            return (root, query, builder) -> builder.lessThanOrEqualTo(root.get(CREATED_AT), endTime);

        if (endTime == null)
            return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get(CREATED_AT), startTime);

        return (root, query, builder) -> builder.between(root.get(CREATED_AT), startTime, endTime);
    }


    public static Specification<User> getSpecification (final UserFilterRequest request) {
        return (root, query, builder) -> {
            query.distinct(true);
            return where(hasName(request.getName()))
                    .and(hasCreatedAt(request.getStartTime(), request.getEndTime()))
                    .toPredicate(root, query, builder);
        };
    }

}
