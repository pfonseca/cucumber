package io.cucumber.cucumberexpressions;

import java.util.List;
import java.util.regex.Pattern;

public interface Expression {
    List<Argument<?>> match(String text, DataTable argument);

    Pattern getRegexp();

    String getSource();
}
