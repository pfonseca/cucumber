package io.cucumber.cucumberexpressions;

public interface ParameterTransformer<T> {
    T transform(String[] groupValues);
}
