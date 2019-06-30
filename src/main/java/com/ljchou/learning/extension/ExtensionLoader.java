package com.ljchou.learning.extension;


import com.ljchou.learning.utils.Holder;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

public class ExtensionLoader<T> {

    private static final String SERVICES_DIRECTORY = "META-INF/services/";

    private static final String DUBBO_DIRECTORY = "META-INF/dubbo";

    private static final String DUBBO_INTERNAL_DIRECTORY = DUBBO_DIRECTORY + "internal/";

    private static final Pattern NAME_SEPARATOR = Pattern.compile("\\s*[,]+\\s*");

    private static final ConcurrentMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();

    private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    private final Class<?> type;

    private final ExtensionFactory objectFactory;

    private final ConcurrentMap<Class<?>, String> cachedNames = new ConcurrentHashMap<>();

    private final Holder<Map<String, Class<?>>>  cachedClasses = new Holder<>();

    private final Map<String, Object> cachedActivates = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    private final Holder<Object> cachedAdaptiveInstance = new Holder<>();

    private volatile Class<?> cachedAdaptiveClass = null;

    private String cachedDefaultName;

    private volatile Throwable createAdaptiveInstanceError;

    private Set<Class<?>> cchedWrapperClasses;

    private Map<String, IllegalStateException> exceptions = new ConcurrentHashMap<>();

    private ExtensionLoader(Class<?> type) {
        this.type = type;
        objectFactory = null;// TODO
//        objectFactory = (type == ExtensionFactory.class ? null : ExtensionLoader.getExtensionLoader(ExtensionFactory.class).get)
    }

    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (null == type) {
            throw new IllegalArgumentException("Extension type == null");
        }

        if (!type.isInterface()) {
            throw new IllegalArgumentException("Extension type (" + type + ") is not an interface!");
        }

        if (!withExtensionAnnotation(type)) {
            throw new IllegalArgumentException("Extension type (" + type +
                    ") is not an extension, because it is NOT annotated with @" + SPI.class.getSimpleName() + "!");
        }

        ExtensionLoader<T> loader = (ExtensionLoader<T>)EXTENSION_LOADERS.get(type);
        if (null == loader) {
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
            loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }

    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(SPI.class);
    }

}
