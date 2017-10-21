package io.cucumber.java;

import io.cucumber.cucumberexpressions.Argument;
import io.cucumber.cucumberexpressions.Group;
import io.cucumber.datatable.RawTableTransformer;

import java.util.List;

public class DataTableArgument<T> implements Argument<T> {


    private final RawTableTransformer<T> tableType;
    private final List<List<String>> argument;

    public DataTableArgument(RawTableTransformer<T> tableType, List<List<String>> argument) {
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
