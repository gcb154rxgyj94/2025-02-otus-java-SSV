package ru.otus.classes;

import ru.otus.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Ioc {

    private static TestLoggingInterface testLoggingProxy = null;

    private Ioc() {}

    public static TestLoggingInterface createMyClass() {
        if (Objects.isNull(testLoggingProxy)) {
            TestLoggingInterface original = new TestLogging();
            InvocationHandler handler = new LogInvocationHandler(original);
            testLoggingProxy = (TestLoggingInterface) Proxy.newProxyInstance(original.getClass().getClassLoader(), original.getClass().getInterfaces(), handler);
        }
        return testLoggingProxy;
    }

    private static class LogInvocationHandler implements InvocationHandler {

        private final Object object;
        private Set<Method> methods;

        private LogInvocationHandler(Object object) {
            this.object = object;
            this.methods = Arrays.stream(object.getClass().getMethods()).collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method targetMethod = methods.stream()
                    .filter(method1 -> method1.getName().equals(method.getName()) && Arrays.equals(method1.getParameterTypes(), method.getParameterTypes()))
                    .findFirst()
                    .orElseThrow(() -> new ClassNotFoundException("Не найден метод " + method.getName() + " с параметрами " + Arrays.toString(method.getParameterTypes())));
            if (targetMethod.isAnnotationPresent(Log.class)) {
                System.out.print("executed method: " + method.getName() + ", params: ");
                Arrays.stream(args)
                        .forEach(
                                arg -> System.out.print(arg.toString() + " ")
                        );
                System.out.println();
            }
            return method.invoke(object, args);
        }

    }

}
