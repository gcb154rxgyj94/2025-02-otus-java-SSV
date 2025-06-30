package ru.otus.appcontainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws Exception{
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) throws Exception {
        checkConfigClass(configClass);
        Object config = createConfig(configClass);
        List<Method> methods = getAppComponentMethods(configClass)
                .stream()
                .sorted(new MethodComparatorByOrder())
                .toList();
        for (Method method : methods) {
            String componentName = method.getAnnotation(AppComponent.class).name();
            if (appComponentsByName.containsKey(componentName)) {
                throw new IllegalArgumentException(String.format("Component with name %s already exist", componentName));
            }
            Object component = createComponent(config, method);
            appComponents.add(component);
            appComponentsByName.put(componentName, component);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object createConfig(Class<?> configClass) throws Exception {
        Constructor<?> constructor;
        try {
            constructor = configClass.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodException(
                    String.format("Config %s doesn't have empty constructor", configClass.getName())
            );
        }
        return constructor.newInstance();
    }

    private List<Method> getAppComponentMethods(Class<?> configClass) {
        return Arrays.stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .toList();
    }

    private Object createComponent(Object config, Method method) {
        method.setAccessible(true);
        List<Class<?>> parameters = List.of(method.getParameterTypes());
        List<Object> params = new ArrayList<>();
        for (Class<?> classParam : parameters) {
            if (!componentExists(classParam)) {
                throw new IllegalStateException(
                        String.format("Not found component %s for create component %s", classParam.getName(), method.getReturnType())
                );
            }
            params.add(appComponents.stream()
                    .filter(appComponent -> classParam.isAssignableFrom(appComponent.getClass()))
                    .findFirst()
                    .get());
        }
        Object component;
        try {
            component = method.invoke(config, params.toArray());
        } catch (Exception e) {
            throw new IllegalStateException(
                    String.format("Error create component %s", method.getReturnType()),
                    e.getCause()
            );
        }
        return component;
    }

    private boolean componentExists(Class<?> componentClass) {
        return appComponents.stream().anyMatch(
                appComponent -> componentClass.isAssignableFrom(appComponent.getClass())
        );
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        boolean componentFind = false;
        C component = null;
        for (Object appComponent : appComponents) {
            if (componentClass.isInstance(appComponent)) {
                if (componentFind) {
                    throw new IllegalArgumentException(String.format("There are more than 1 component %s", componentClass.getName()));
                }
                componentFind = true;
                component = (C) appComponent;
            }
        }
        if (!componentFind) {
            throw new IllegalArgumentException(String.format("No component %s", componentClass.getName()));
        }
        return component;
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        }
        throw new IllegalArgumentException(String.format("No component with name %s", componentName));
    }
}
