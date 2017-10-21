package io.cucumber.datatable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public final class DataTableTypeRegistry {

    private final Map<String, DataTableType<?>> tableTypeByName = new HashMap<>();
    private final Map<String, DataTableType<?>> tableTypeByType = new HashMap<>(); //TODO: Should be Map<String, List<DataTableType<?>>

    public DataTableTypeRegistry() {

    }

    public void defineDataTableType(DataTableType<?> dataTableType) {
        if (tableTypeByName.containsKey(dataTableType.getName()))
            throw new DuplicateTypeNameException(String.format("There is already a data table type with name %s", dataTableType.getName()));

        tableTypeByName.put(dataTableType.getName(), dataTableType);
        tableTypeByType.put(dataTableType.getType().toString(), dataTableType);
    }


    public <T> DataTableType<T> lookupTableTypeByType(final Type tableType) {
        return (DataTableType<T>) tableTypeByType.get(tableType.toString());
    }


    public <T> DataTableType<T> lookupTableTypeByName(String tableType) {
        return (DataTableType<T>) tableTypeByName.get(tableType);
    }
}


