package pgdp.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;


public class DynamicMethod<T> {
    final DynamicClass<?> dClass;
    final String mName;
    final DynamicClass<?>[] params;
    final DynamicClass<T> returnC;
    Method m;

    public DynamicMethod(DynamicClass<?> dClass, Class<T> dynamicableReturn, String mName,
                         Object... dynamicableParams) {
        this(dClass, DynamicClass.toDynamic(dynamicableReturn), mName, dynamicableParams);
    }

    public DynamicMethod(DynamicClass<?> dClass, DynamicClass<T> dynamicableReturn, String mName,
                         Object... dynamicableParams) {
        this.dClass = Objects.requireNonNull(dClass);
        this.mName = Objects.requireNonNull(mName);
        this.returnC = Objects.requireNonNull(dynamicableReturn);
        this.params = DynamicClass.toDynamic(Objects.requireNonNull(dynamicableParams));
    }

    public boolean isCallable() {
        try {
            getM();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Method getM() {
        if (m == null) {
            try {
                m = dClass.getC().getDeclaredMethod(mName, DynamicClass.resolveAll(params));
                m.trySetAccessible();
                if (!returnC.getC().isAssignableFrom(m.getReturnType()))
                    throw new TaskNotCompletedException("Methode " + mName + " mit Parametern " + descParams(params) + " gibt nicht " + returnC
                            + " zurück");
            } catch (NoSuchMethodException e) {
                throw new TaskNotCompletedException("Keine öffentliche Methode " + returnC + " " + this + " gefunden.");
            }
        }
        return m;
    }

    public T invokeOn(Object o, Object... params) {
        try {
            return returnC.cast(getM().invoke(o, params));
        } catch (IllegalAccessException e) {
            throw new TaskNotCompletedException("Methode " + this + " konnte nicht aufgerufen werden, Zugriff auf die Methode nicht möglich");
        } catch (IllegalArgumentException e) {
            throw new TaskNotCompletedException("Methode " + this + " konnte Parametertypen " + descArgs(params) + " für Objekt " + o
                    + " nicht entgegennehmen");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof RuntimeException)
                throw (RuntimeException) e.getTargetException();
            throw new RuntimeException(e.getTargetException());
        } catch (ClassCastException e) {
            throw new TaskNotCompletedException("Rückgabe von " + mName + " mit Parametern " + descParams(this.params) + " der Klasse " + dClass
                    + " kann nicht nach " + returnC + "gecastet werden");
        }
    }

    public void invokeOnVoid(Object o, Object... params) {
        try {
           getM().invoke(o, params);
        } catch (IllegalAccessException e) {
            throw new TaskNotCompletedException("Methode " + this + " konnte nicht aufgerufen werden, Zugriff auf die Methode nicht möglich");
        } catch (IllegalArgumentException e) {
            throw new TaskNotCompletedException("Methode " + this + " konnte Parametertypen " + descArgs(params) + " für Objekt " + o
                    + " nicht entgegennehmen");
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof RuntimeException)
                throw (RuntimeException) e.getTargetException();
            throw new RuntimeException(e.getTargetException());
        }
    }

    public T invokeStatic(Object... params) {
        if (!Modifier.isStatic(getM().getModifiers()))
            throw new TaskNotCompletedException ("Methode " + this + " ist nicht statisch");
        return invokeOn(null, params);
    }

    public String signature() {
        return mName + descParams(params);
    }

    public static String signatureOf(Method method) {
        return method.getName() + descParams(DynamicClass.toDynamic(method.getParameterTypes()));
    }

    public static String[] signatureOfAll(Method... methods) {
        return Arrays.stream(methods).map(DynamicMethod::signatureOf).toArray(String[]::new);
    }

    public static String[] signatureOfAll(DynamicMethod<?>... methods) {
        return Arrays.stream(methods).map(DynamicMethod::signature).toArray(String[]::new);
    }

    public static String descArgs(Object... args) {
        return Arrays.stream(args).map(x -> x == null ? null : args.getClass().getCanonicalName())
                .collect(Collectors.joining(", ", "(", ")"));
    }

    public static String descParams(DynamicClass<?>... params) {
        return Arrays.stream(params).map(DynamicClass::getName).collect(Collectors.joining(", ", "(", ")"));
    }
}
