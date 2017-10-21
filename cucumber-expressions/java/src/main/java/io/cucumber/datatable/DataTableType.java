package io.cucumber.datatable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public final class DataTableType<T> implements Comparable<DataTableType<?>> {

    private static final ConversionRequired CONVERSION_REQUIRED = new ConversionRequired();

    private final String name;
    private final Type type;
    private final RawTableTransformer<T> transformer;

    public DataTableType(String name, Type type, RawTableTransformer<T> transformer) {
        if (name == null) throw new CucumberDataTableException("name cannot be null");
        if (type == null) throw new CucumberDataTableException("type cannot be null");
        if (transformer == null) throw new CucumberDataTableException("transformer cannot be null");
        this.name = name;
        this.type = type;
        this.transformer = transformer;
    }

    public DataTableType(String name, Type type, final TableTransformer<T> transformer) {
        this(name, type, new RawTableTransformer<T>() {
            @Override
            public T transform(List<List<String>> raw) {
                return transformer.transform(new DataTable(raw, CONVERSION_REQUIRED));
            }
        });
    }

    public T transform(List<List<String>> raw) {
        return transformer.transform(raw);
    }

    public int compareTo(DataTableType<?> o) {
        return name.compareTo(o.name);
    }


    public String getName() {
        return name;
    }


    public Type getType() {
        return type;
    }

    public static <T> DataTableType<T> tableAs(String name, Class<T> type, final TableTransformer<T> transformer) {
        return new DataTableType<>(name, type, transformer);
    }

    public static <T> DataTableType<List<T>> tableOf(String name, Class<T> type, final TableRowTransformer<T> transformer) {
        return new DataTableType<>(name, aListOf(type), new TableTransformer<List<T>>() {
            @Override
            public List<T> transform(DataTable table) {
                List<T> list = new ArrayList<>();

                for (Map<String, String> tableRow : table.asMaps()) {
                    list.add(transformer.transform(tableRow));
                }

                return list;
            }
        });
    }

    public static Type aListOf(final Type type) {
        //TODO: Quick fake out. This works because we the parameter registry uses toString.
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{type};
            }

            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public String toString() {
                if (type instanceof Class) {
                    return List.class.getName() + "<" + ((Class) type).getName() + ">";
                }

                return List.class.getName() + "<" + type.toString() + ">";
            }
        };
    }
}
