package io.cucumber.java;

import io.cucumber.cucumberexpressions.Argument;
import io.cucumber.cucumberexpressions.Group;
import io.cucumber.datatable.DocStringTransformer;

public final class DocStringArgument<T> implements Argument<T> {

    private final DocStringTransformer<T> docStringType;
    private final String argument;

    public DocStringArgument(DocStringTransformer<T> docStringType, String argument) {
        this.docStringType = docStringType;
        this.argument = argument;
    }

    @Override
    public Group getGroup() {
        return null;
    }

    @Override
    public T getValue() {
        return docStringType.transform(argument);
    }
}
