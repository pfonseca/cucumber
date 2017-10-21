package io.cucumber.datatable;

import java.util.Map;

public interface TableRowTransformer<T> {
    T transform(Map<String, String> tableRow);
}
