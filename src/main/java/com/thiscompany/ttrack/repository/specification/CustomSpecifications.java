package com.thiscompany.ttrack.repository.specification;

import com.thiscompany.ttrack.model.Task;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class CustomSpecifications {

    public static Specification<Task> filterByAuthUser(String username) {
        return ((root, query, cb)
                -> cb.equal(root.get("owner").get("username"), username));
    }

    public static <T, E> Specification<T> filterByUserAndParam(
            String username,
            Function<Root<T>, Path<E>> fieldPathExtractor,
            E param
    ) {
        return (root, query, cb) -> cb.and(
                cb.equal(root.get("owner").get("username"), username),
                cb.equal(fieldPathExtractor.apply(root), param)
        );
    }

    public static Specification<Task> filterByParam(
        String requestUser,
        String title,
        String details,
        String status,
        String state,
        String priority,
        Boolean isCompleted
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = Stream.of(
                Optional.of(requestUser).map(u -> cb.equal(root.get("owner").get("username"), u)),
                Optional.ofNullable(title).map(t -> cb.like(cb.lower(root.get("title")), "%" + t.toLowerCase() + "%")),
                Optional.ofNullable(details).map(d -> cb.like(cb.lower(root.get("details")), "%" + d.toLowerCase() + "%")),
                Optional.ofNullable(status).map(s -> cb.equal(root.get("status"), s)),
                Optional.ofNullable(state).map(s -> cb.equal(root.get("state"), s)),
                Optional.ofNullable(priority).map(p -> cb.equal(root.get("priority"), p)),
                Optional.ofNullable(isCompleted).map(c -> cb.equal(root.get("isCompleted"), c))
            ).flatMap(Optional::stream).toList();

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
