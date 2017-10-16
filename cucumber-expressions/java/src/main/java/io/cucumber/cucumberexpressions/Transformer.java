package io.cucumber.cucumberexpressions;

public interface Transformer<F,T> {
    T transform(F values);
}
