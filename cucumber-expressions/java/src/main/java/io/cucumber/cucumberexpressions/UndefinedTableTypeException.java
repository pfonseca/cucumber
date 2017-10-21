package io.cucumber.cucumberexpressions;

public class UndefinedTableTypeException extends CucumberExpressionException {
    public UndefinedTableTypeException(String typeName) {
        super(String.format("Undefined table type {%s}", typeName));
    }
}
