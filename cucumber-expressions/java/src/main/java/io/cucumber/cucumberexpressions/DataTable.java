package io.cucumber.cucumberexpressions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataTable {
    private final List<List<String>> rows;
    private final List<String> headers;
    private final List<List<String>> raw;

    public DataTable(List<List<String>> raw) {
        this.raw = raw;
        this.headers = raw.isEmpty() ? Collections.<String>emptyList() : raw.get(0);
        this.rows = raw.size() <= 1 ? Collections.<List<String>>emptyList() : raw.subList(1, raw.size());

    }

    public List<Map<String, String>> rows() {
        List<Map<String, String>> headersAndRows = new ArrayList<>();
        for (List<String> row : rows) {
            LinkedHashMap<String, String> headersAndRow = new LinkedHashMap<>();
            for (int i = 0; i < headers.size(); i++) {
                headersAndRow.put(headers.get(i), row.get(i));
            }
            headersAndRows.add(headersAndRow);
        }

        return headersAndRows;
    }

    public DataTable transpose() {
        List<List<String>> transposed = new ArrayList<List<String>>();
        for (List<String> pickleRow : raw) {
            for (int j = 0; j < pickleRow.size(); j++) {
                List<String> row = null;
                if (j < transposed.size()) {
                    row = transposed.get(j);
                }
                if (row == null) {
                    row = new ArrayList<>();
                    transposed.add(row);
                }
                row.add(pickleRow.get(j));
            }
        }
        return new DataTable(transposed);
    }
}