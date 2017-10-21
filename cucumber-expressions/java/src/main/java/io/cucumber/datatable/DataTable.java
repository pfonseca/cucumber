package io.cucumber.datatable;

import io.cucumber.cucumberexpressions.CucumberExpressionException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DataTable {
    private final List<List<String>> raw;

    private final TableConverter tableConverter;

    public DataTable(List<List<String>> raw) {
        this(raw, new NoConverterDefined());
    }


    public DataTable(List<List<String>> raw, TableConverter tableConverter) {
        if (raw == null) throw new CucumberExpressionException("cells can not be null");
        this.raw = raw;
        this.tableConverter = tableConverter;
    }


    public List<String> topRow() {
        if (raw.isEmpty()) {
            return null;
        }

        return raw.get(0);
    }

    public List<List<String>> rows(int fromRow) {
        return rows(fromRow, raw.size());
    }

    public List<List<String>> rows(int fromRow, int toRow) {
        return raw.subList(fromRow, toRow);
    }

    public List<List<String>> cells() {
        return raw;
    }

    public DataTable transpose() {
        List<List<String>> transposed = new ArrayList<>();
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
        return new DataTable(transposed, tableConverter);
    }



    public <T> T convert(Type type, boolean transposed) {
        return tableConverter.convert(this, type, transposed);
    }

    public <T> List<T> asList(Type itemType) {
        return tableConverter.toList(this, itemType);
    }

    public <T> List<List<T>> asLists(Type itemType) {
        return tableConverter.toLists(this, itemType);
    }

    public <K, V> Map<K, V> asMap(Type keyType, Type valueType) {
        return tableConverter.toMap(this, keyType, valueType);
    }

    public <K, V> List<Map<K, V>> asMaps(Type keyType, Type valueType) {
        return tableConverter.toMaps(this, keyType, valueType);
    }

    public List<Map<String, String>> asMaps() {
        List<String> headers = topRow();
        List<List<String>> rows = rows(1);


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

    private static final class NoConverterDefined implements TableConverter {

        @Override
        public <T> T convert(DataTable dataTable, Type type, boolean transposed) {
            throw new CucumberExpressionException(String.format("Can't convert DataTable to %s. DataTable was created without a converter", type));
        }

        @Override
        public <T> List<T> toList(DataTable dataTable, Type itemType) {
            throw new CucumberExpressionException(String.format("Can't convert DataTable to List<%s>. DataTable was created without a converter", itemType));
        }

        @Override
        public <T> List<List<T>> toLists(DataTable dataTable, Type itemType) {
            throw new CucumberExpressionException(String.format("Can't convert DataTable to List<List<%s>>. DataTable was created without a converter", itemType));
        }

        @Override
        public <K, V> Map<K, V> toMap(DataTable dataTable, Type keyType, Type valueType) {
            throw new CucumberExpressionException(String.format("Can't convert DataTable to Map<%s,%s>. DataTable was created without a converter", keyType, valueType));
        }

        @Override
        public <K, V> List<Map<K, V>> toMaps(DataTable dataTable, Type keyType, Type valueType) {
            throw new CucumberExpressionException(String.format("Can't convert DataTable to List<Map<%s,%s>>. DataTable was created without a converter", keyType, valueType));
        }

        @Override
        public DataTable toTable(List<?> objects, String... columnNames) {
            throw new CucumberExpressionException("Can't create a DataTable. DataTable was created without a converter");
        }
    }
}