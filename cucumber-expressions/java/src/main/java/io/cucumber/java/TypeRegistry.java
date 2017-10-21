package io.cucumber.java;

import io.cucumber.cucumberexpressions.ParameterType;
import io.cucumber.cucumberexpressions.ParameterTypeRegistry;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.DataTableTypeRegistry;

import java.lang.reflect.Type;
import java.util.Locale;

public final class TypeRegistry {

    private final ParameterTypeRegistry parameterTypeRegistry;

    private final DataTableTypeRegistry dataTableTypeRegistry;


    public TypeRegistry(Locale locale) {
        parameterTypeRegistry = new ParameterTypeRegistry(locale);
        dataTableTypeRegistry = new DataTableTypeRegistry();
    }

    ParameterTypeRegistry parameterTypeRegistry() {
        return parameterTypeRegistry;
    }

    public void defineParameterType(ParameterType<?> parameterType) {
        parameterTypeRegistry.defineParameterType(parameterType);
    }

    public void defineDataTableType(DataTableType<?> tableType) {
        dataTableTypeRegistry.defineDataTableType(tableType);
    }

    public <T> ParameterType<T> lookupParameterTypeByType(Type itemType) {
        return parameterTypeRegistry.lookupByType(itemType);
    }

    public <T> ParameterType<T> lookupParameterTypeByTypeName(String typeName) {
        return parameterTypeRegistry.lookupByTypeName(typeName);
    }

    public <T> DataTableType<T> lookupTableTypeByType(Type type) {
        return dataTableTypeRegistry.lookupTableTypeByType(type);
    }

    public <T> DataTableType<T> lookupTableTypeByName(String tableType) {
        return dataTableTypeRegistry.lookupTableTypeByName(tableType);
    }
}
