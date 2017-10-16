package io.cucumber.cucumberexpressions;

public interface Argument<T> {
    Group getGroup();

    T getValue();
}
