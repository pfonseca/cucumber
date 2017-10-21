package io.cucumber.cucumberexpressions;

import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.ParameterTypeRegistryTableConverter;

import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates a {@link CucumberExpression} or {@link RegularExpression} from a {@link String}
 * using heuristics. This is particularly useful for languages that don't have a
 * literal syntax for regular expressions such as Ruby or JavaScript - with Java
 * a regular expression has to be represented as a String.
 */
public class ExpressionFactory {

    private static final Pattern BEGIN_ANCHOR = Pattern.compile("^\\^.*");
    private static final Pattern END_ANCHOR = Pattern.compile(".*\\$$");
    private static final Pattern SCRIPT_STYLE_REGEXP = Pattern.compile("^/(.*)/$");
    private static final Pattern PARENS = Pattern.compile("\\(([^)]+)\\)");
    private static final Pattern ALPHA = Pattern.compile("[a-zA-Z]+");
    private final ParameterTypeRegistry parameterTypeRegistry;
    private final ParameterTypeRegistryTableConverter tableConverter;

    public ExpressionFactory(ParameterTypeRegistry parameterTypeRegistry) {
        this.parameterTypeRegistry = parameterTypeRegistry;
        this.tableConverter = new ParameterTypeRegistryTableConverter(parameterTypeRegistry);
    }

    public Expression createExpression(String expressionString, final Type tableType) {
        if (tableType == null) throw new CucumberExpressionException("tableType can not be null");

        return createExpression(expressionString, new Transformer<List<List<String>>, Object>() {
            @Override
            public Object transform(List<List<String>> raw) {
                return new DataTable(raw, tableConverter).convert(tableType, false);
            }
        });
    }

    public Expression createExpression(String expressionString, final String tableType) {
        if (tableType == null) throw new CucumberExpressionException("tableType can not be null");

        final TableType<?> type = parameterTypeRegistry.lookupTableTypeByName(tableType);

        if (type == null) {
            throw new UndefinedTableTypeException(tableType);
        }

        return createExpression(expressionString, new Transformer<List<List<String>>, Object>() {
            @Override
            public Object transform(List<List<String>> raw) {
                return type.transform(new DataTable(raw, tableConverter));
            }
        });
    }

    private Expression createExpression(String expressionString, Transformer<List<List<String>>, ?> tableType) {
        Matcher m = BEGIN_ANCHOR.matcher(expressionString);
        if (m.find()) {
            return new RegularExpression(Pattern.compile(expressionString), tableType, parameterTypeRegistry);
        }
        m = END_ANCHOR.matcher(expressionString);
        if (m.find()) {
            return new RegularExpression(Pattern.compile(expressionString), tableType, parameterTypeRegistry);
        }
        m = SCRIPT_STYLE_REGEXP.matcher(expressionString);
        if (m.find()) {
            return new RegularExpression(Pattern.compile(m.group(1)), tableType, parameterTypeRegistry);
        }
        m = PARENS.matcher(expressionString);
        if (m.find()) {
            String insideParens = m.group(1);
            if (ALPHA.matcher(insideParens).lookingAt()) {
                return new CucumberExpression(expressionString, tableType, parameterTypeRegistry);
            }
            return new RegularExpression(Pattern.compile(expressionString), tableType, parameterTypeRegistry);
        }
        return new CucumberExpression(expressionString, tableType, parameterTypeRegistry);
    }
}
