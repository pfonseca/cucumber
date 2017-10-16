package io.cucumber.cucumberexpressions;

import java.lang.reflect.Type;
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

    public ExpressionFactory(ParameterTypeRegistry parameterTypeRegistry) {
        this.parameterTypeRegistry = parameterTypeRegistry;
    }

    public Expression createExpression(String expressionString, Type tableType) {
        return createExpression(expressionString, parameterTypeRegistry.lookupTableTypeByType(tableType));
    }

    public Expression createExpression(String expressionString, String tableType) {
        return createExpression(expressionString, parameterTypeRegistry.lookupTableTypeByName(tableType));
    }

    private Expression createExpression(String expressionString, TableType tableType) {
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
