package com.test.task.repositories.specifications;

import com.test.task.entities.Bank;
import com.test.task.entities.Client;
import com.test.task.entities.Form;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;

public class ClientSpecifications {
    public static Specification<Client> addressLike(String address) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(root.get("address"), address);
    }

    public static Specification<Client> formIs(Form form) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("form"), form);
    }

//    public static Specification<Client> bankIs(Bank bank) {
//        return (root, criteriaQuery, criteriaBuilder) ->
//                criteriaBuilder.equal(root.get("bank"), bank);
//    }

    public static Specification<Client> clientIs(Long client) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), client);
    }

    public static Specification<Client> nameEquals(String name) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("name"), name);
    }

//
//    public static Specification<Product> priceLesserOrEqualsThan(int maxPrice) {
//        return (root, criteriaQuery, criteriaBuilder) ->
//                criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
//    }
//
//    public static Specification<Product> titleContainsFollowingExpression(String title) {
//        return (root, criteriaQuery, criteriaBuilder) ->
//                criteriaBuilder.like(root.get("title"), String.format("%%%s%%", title));
//    }
//
//    public static Specification<Product> categoryIs(Category category){
//        return (root , criteriaQuery, criteriaBuilder) -> {
//            return criteriaBuilder.isMember(category, root.get("categories"));
//        };
//    }
}
