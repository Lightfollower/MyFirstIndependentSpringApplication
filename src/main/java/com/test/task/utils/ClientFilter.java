package com.test.task.utils;

import com.test.task.entities.Client;
import com.test.task.repositories.specifications.ClientSpecifications;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

@Getter
public class ClientFilter {
    private Specification<Client> spec;
    private StringBuilder filterDefinition;

    public ClientFilter(Map<String, String> map) {
        this.spec = Specification.where(null);
//        this.filterDefinition = new StringBuilder();
            if (map.containsKey("address")) {
                String address = map.get("address");
                spec = spec.and(ClientSpecifications.addressLike(address));
//                filterDefinition.append("&min_price=").append(minPrice);
            }
//            if (map.containsKey("max_price") && !map.get("max_price").isEmpty()) {
//                int maxPrice = Integer.parseInt(map.get("max_price"));
//                spec = spec.and(ProductSpecifications.priceLesserOrEqualsThan(maxPrice));
//                filterDefinition.append("&max_price=").append(maxPrice);
//            }
//            if (map.containsKey("title") && !map.get("title").isEmpty()) {
//                String title = map.get("title");
//                spec = spec.and(ProductSpecifications.titleContainsFollowingExpression(title));
//                filterDefinition.append("&title=").append(title);
//            }
//            if (categories != null && !categories.isEmpty()) {
//                Specification specCategories = null;
//                for (Category c : categories) {
//                    if (specCategories == null) {
//                        specCategories = ProductSpecifications.categoryIs(c);
//                    } else {
//                        specCategories = specCategories.or(ProductSpecifications.categoryIs(c));
//                    }
//                }
//                spec = spec.and(specCategories);
//            }
    }
}
