package com.thiscompany.ttrack.repository.specification;

import com.thiscompany.ttrack.model.Task;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.function.Function;

public class CustomSpecification {

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

}
