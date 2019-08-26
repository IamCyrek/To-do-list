package com.example.model.specification;

import com.example.model.Task;
import com.example.model.Task_;
import com.example.model.dto.TaskFilterRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

import static org.springframework.data.jpa.domain.Specification.where;

public class TaskSpecification {
    private static Specification<Task> hasContent(final String content) {
        if (content == null)
            return null;

        return (root, query, builder) ->
                builder.like(builder.lower(root.get(Task_.content)), "%" + content.toLowerCase() + "%");
    }

    private static Specification<Task> hasPriority(final Integer priority) {
        if (priority == null)
            return null;

        return (root, query, builder) -> builder.equal(root.get(Task_.priority), priority);
    }

    private static Specification<Task> hasCreationTime(final Date startTime, final Date endTime) {
        if (startTime == null && endTime == null)
            return null;

        if (startTime == null)
            return (root, query, builder) -> builder.lessThanOrEqualTo(root.get(Task_.creationTime), endTime);

        if (endTime == null)
            return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get(Task_.creationTime), startTime);

        return (root, query, builder) -> builder.between(root.get(Task_.creationTime), startTime, endTime);
    }

    private static Specification<Task> hasIsRemoved(final Boolean isRemoved) {
        if (isRemoved == null)
            return null;

        return (root, query, builder) -> builder.equal(root.get(Task_.isRemoved), isRemoved);
    }


    public static Specification<Task> getSpecification (final TaskFilterRequest request) {
        return (root, query, builder) -> {
            query.distinct(true);
            return where(hasContent(request.getContent()))
                    .and(hasPriority(request.getPriority()))
                    .and(hasCreationTime(request.getStartTime(), request.getEndTime()))
                    .and(hasIsRemoved(request.getIsRemoved()))
                    .toPredicate(root, query, builder);
        };
    }
}
