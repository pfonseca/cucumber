package io.cucumber.datatable;


public class UndefinedTableTypeException extends CucumberDataTableException {
    public UndefinedTableTypeException(String typeName) {
        super(String.format("Undefined table type {%s}", typeName));
    }
}
