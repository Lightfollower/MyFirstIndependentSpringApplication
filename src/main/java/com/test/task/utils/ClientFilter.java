package com.test.task.utils;

import com.test.task.entities.Bank;
import com.test.task.entities.Client;
import com.test.task.entities.Form;
import com.test.task.repositories.specifications.ClientSpecifications;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class ClientFilter {
    private Specification<Client> spec;

    public ClientFilter(Map<String, String> map, Form formFilter, Set<Long> clientSet) {
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
                spec = spec.and(ClientSpecifications.formIs(formFilter));
            }
//            if (bankFilter != null) {
//                spec = spec.and(ClientSpecifications.bankIs(bankFilter));
//            }
        if (clientSet != null && !clientSet.isEmpty()) {
            Specification specClients = null;
            for (Long c : clientSet) {
                if (specClients == null) {
                    specClients = ClientSpecifications.clientIs(c);
                } else {
                    specClients = specClients.or(ClientSpecifications.clientIs(c));
                }
            }
            spec = spec.and(specClients);
        }
    }
}
