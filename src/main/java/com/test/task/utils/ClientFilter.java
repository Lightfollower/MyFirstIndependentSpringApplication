package com.test.task.utils;

import com.test.task.entities.Client;
import com.test.task.entities.Form;
import com.test.task.repositories.specifications.ClientSpecifications;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

@Getter
public class ClientFilter {
    private Specification<Client> spec;

    public ClientFilter(Map<String, String> map, Form formFilter) {
        this.spec = Specification.where(null);
//        this.filterDefinition = new StringBuilder();
            if (map.containsKey("address")) {
                String address = map.get("address");
                spec = spec.and(ClientSpecifications.addressLike(address));
            }
//            if (map.containsKey("form")) {
//                String form = map.get("form");
//                spec = spec.and(ClientSpecifications.formEquals(form));
//            }
//            if (map.containsKey("title") && !map.get("title").isEmpty()) {
//                String title = map.get("title");
//                spec = spec.and(ProductSpecifications.titleContainsFollowingExpression(title));
//                filterDefinition.append("&title=").append(title);
//            }
            if (formFilter != null) {
                Specification specCategories = null;
                    if (specCategories == null) {
                        specCategories = ClientSpecifications.formIs(formFilter);
                    } else {
                        specCategories = specCategories.or(ClientSpecifications.formIs(formFilter));
                    }
                spec = spec.and(specCategories);
            }
    }
}
