package ru.otus.classes;

import ru.otus.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Objects;

public class Ioc {

    private static TestLoggingInterface testLogging = null;

    private Ioc() {}

    public static TestLoggingInterface createMyClass() {
        if (Objects.isNull(testLogging)) {
            TestLoggingInterface original = new TestLogging();
            InvocationHandler handler = new LogInvocationHandler(original);
            testLogging = (TestLoggingInterface) Proxy.newProxyInstance(original.getClass().getClassLoader(), original.getClass().getInterfaces(), handler);
        }
        return testLogging;
    }

    private static class LogInvocationHandler implements InvocationHandler {

        private final TestLoggingInterface testLogging;

        private LogInvocationHandler(TestLoggingInterface testLogging) {
            this.testLogging = testLogging;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method targetMethod = testLogging.getClass().getMethod(method.getName(), method.getParameterTypes());
            if (targetMethod.isAnnotationPresent(Log.class)) {
                System.out.print("executed method: " + method.getName() + ", params: ");
                Arrays.stream(args)
                        .forEach(
                                arg -> System.out.print(arg.toString() + " ")
                        );
                System.out.println();
            }
            return method.invoke(testLogging, args);
        }

    }

}
