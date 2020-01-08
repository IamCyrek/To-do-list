package com.example.model.specification;

import com.example.controller.dto.TaskFilterRequest;
import com.example.model.Task;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

import static org.springframework.data.jpa.domain.Specification.where;

public class TaskSpecification {
    private static final String CONTENT = "content";
    private static final String PRIORITY = "priority";
    private static final String CREATION_TIME = "creationTime";
    private static final String IS_REMOVED = "isRemoved";

    private static Specification<Task> hasContent(final String content) {
        if (content == null)
            return null;

        return (root, query, builder) ->
                builder.like(builder.lower(root.get(CONTENT)), "%" + content.toLowerCase() + "%");
    }

    private static Specification<Task> hasPriority(final Integer priority) {
        if (priority == null)
            return null;

        return (root, query, builder) -> builder.equal(root.get(PRIORITY), priority);
    }

    private static Specification<Task> hasCreationTime(final LocalDateTime startTime, final LocalDateTime endTime) {
        if (startTime == null && endTime == null)
            return null;

        if (startTime == null)
            return (root, query, builder) -> builder.lessThanOrEqualTo(root.get(CREATION_TIME), endTime);

        if (endTime == null)
            return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get(CREATION_TIME), startTime);

        return (root, query, builder) -> builder.between(root.get(CREATION_TIME), startTime, endTime);
    }

    private static Specification<Task> hasIsRemoved(final Boolean isRemoved) {
        if (isRemoved == null)
            return null;

        return (root, query, builder) -> builder.equal(root.get(IS_REMOVED), isRemoved);
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
