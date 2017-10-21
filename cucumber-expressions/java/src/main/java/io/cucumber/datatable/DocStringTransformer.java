package io.cucumber.datatable;


public interface DocStringTransformer<T> {
    T transform(String table);
}
