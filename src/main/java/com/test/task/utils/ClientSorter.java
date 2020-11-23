package com.test.task.utils;

import lombok.Data;
import org.springframework.data.domain.Sort;

import java.util.Map;

@Data
public class ClientSorter {
    private Sort sort;

    public ClientSorter(Map<String, String> requestParams) {
        this.sort = Sort.unsorted();
        if (requestParams.containsKey("form_sort")) {
            String direction = requestParams.get("form_sort");
            switch (direction) {
                case "asc":
                    sort = sort.and(Sort.by(Sort.Direction.ASC, "form.id"));
                    break;
                case "desc":
                    sort = sort.and(Sort.by(Sort.Direction.DESC, "form.id"));
                    break;
            }
        }
        if (requestParams.containsKey("name_sort")) {
            String direction = requestParams.get("name_sort");
            switch (direction) {
                case "asc":
                    sort = sort.and(Sort.by(Sort.Direction.ASC, "name"))/*) = pec.and(ClientSpecifications.sortByNameSpec(direction));*/;
                    break;
                case "desc":
                    sort = sort.and(Sort.by(Sort.Direction.DESC, "name"))/*) = pec.and(ClientSpecifications.sortByNameSpec(direction));*/;
                    break;
            }
        }
    }
}
