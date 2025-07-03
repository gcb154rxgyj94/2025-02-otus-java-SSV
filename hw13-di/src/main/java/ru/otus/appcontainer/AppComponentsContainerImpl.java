package ru.otus.appcontainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(String classPath) throws Exception {
        Reflections reflections = new Reflections(classPath);
        Class<?>[] annotatedClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class).toArray(new Class<?>[0]);
        for (Class<?> configClass : orderAppComponentsContainerConfig(annotatedClasses)) {
            processConfig(configClass);
        }
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) throws Exception{
        Arrays.stream(initialConfigClass).forEach(this::checkConfigClass);
        for (Class<?> configClass : orderAppComponentsContainerConfig(initialConfigClass)) {
            processConfig(configClass);
        }
    }

    private void processConfig(Class<?> configClass) throws Exception {
        Object config = createConfig(configClass);
        List<Method> methods = getOrderedAppComponentMethods(configClass);
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

    private List<Class<?>> orderAppComponentsContainerConfig(Class<?>... configClasses) {
        return Arrays.stream(configClasses)
                .sorted(new AppComponentsContainerConfigComparatorByOrder())
                .toList();
    }

    private List<Method> getOrderedAppComponentMethods(Class<?> configClass) {
        return Arrays.stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(new AppComponentComparatorByOrder())
                .toList();
    }

    private Object createComponent(Object config, Method method) {
        method.setAccessible(true);
        List<Object> params = new ArrayList<>();
        for (Class<?> classParam : List.of(method.getParameterTypes())) {
            if (!componentExists(classParam)) {
                throw new IllegalStateException(
                        String.format("Not found component %s for create component %s", classParam.getName(), method.getReturnType())
                );
            }
            params.add(getAppComponent(classParam));
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
        List<Object> components = appComponents.stream()
                .filter(appComponent -> componentClass.isAssignableFrom(appComponent.getClass()))
                .toList();
        if (components.isEmpty()) {
            throw new IllegalArgumentException(String.format("No component %s", componentClass.getName()));
        } else if (components.size() > 1) {
            throw new IllegalArgumentException(String.format("There are more than 1 component %s", componentClass.getName()));
        } else {
            return (C) components.get(0);
        }
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        }
        throw new IllegalArgumentException(String.format("No component with name %s", componentName));
    }
}
