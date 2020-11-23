package com.test.task.utils;

import com.test.task.entities.Client;
import com.test.task.entities.Form;
import com.test.task.repositories.specifications.ClientSpecifications;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
import java.util.Set;

@Getter
public class ClientFilter {
    private Specification<Client> spec;

    public ClientFilter(Map<String, String> requestParams, Form formFilter, Set<Long> clientSet) {
        this.spec = Specification.where(null);
            if (requestParams.containsKey("address")) {
                String address = requestParams.get("address");
                spec = spec.and(ClientSpecifications.addressLike(address));
            }
            if (requestParams.containsKey(Constants.CLIENT_STRING)) {
                String name = requestParams.get(Constants.CLIENT_STRING);
                spec = spec.and(ClientSpecifications.nameEquals(name));
            }

//            Попытка сортировать спецификациями
//            if(requestParams.containsKey("name_sort")){
//                String direction = requestParams.get("name_sort");
//                spec = spec.and(ClientSpecifications.sortByNameSpec(direction));
//            }
//            if(requestParams.containsKey("form_sort")){
//                String direction = requestParams.get("form_sort");
//                spec = spec.and(ClientSpecifications.sortByFormSpec(direction));
//            }
            if (formFilter != null) {
                spec = spec.and(ClientSpecifications.formIs(formFilter));
            }

        //        Фильтрация клиентов по названию банка.
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
