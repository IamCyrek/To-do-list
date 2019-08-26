package com.example.model.specification;

import com.example.model.User;
import com.example.model.User_;
import com.example.model.dto.UserFilterRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

import static org.springframework.data.jpa.domain.Specification.where;

public class UserSpecification {
    private static Specification<User> hasName(final String name) {
        if (name == null)
            return null;

        return (root, query, builder) ->
                builder.like(builder.lower(root.get(User_.name)), "%" + name.toLowerCase() + "%");
    }

    private static Specification<User> hasCreatedAt(final Date startTime, final Date endTime) {
        if (startTime == null && endTime == null)
            return null;

        if (startTime == null)
            return (root, query, builder) -> builder.lessThanOrEqualTo(root.get(User_.createdAt), endTime);

        if (endTime == null)
            return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get(User_.createdAt), startTime);

        return (root, query, builder) -> builder.between(root.get(User_.createdAt), startTime, endTime);
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
