package io.cucumber.cucumberexpressions;

import java.util.List;

class TableArgument<T> implements Argument<T> {


    private final Transformer<List<List<String>>, T> tableType;
    private final List<List<String>> argument;

    TableArgument(Transformer<List<List<String>>, T> tableType, List<List<String>> argument) {
        this.tableType = tableType;
        this.argument = argument;
    }

    @Override
    public Group getGroup() {
        return null;
    }

    @Override
    public T getValue() {
        return tableType.transform(argument);
    }
}
