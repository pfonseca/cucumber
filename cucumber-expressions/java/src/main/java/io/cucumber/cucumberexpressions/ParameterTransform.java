package io.cucumber.cucumberexpressions;

import java.lang.reflect.Type;

public interface ParameterTransform<F,T> {
    String getName();

    Type getType();

    T transform(F groupValues);
}
