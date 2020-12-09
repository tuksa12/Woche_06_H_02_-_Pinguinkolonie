package pgdp.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class DynamicConstructor<T> {

	final DynamicClass<T> dClass;
	final DynamicClass<?>[] params;
	Constructor<T> c;

	public DynamicConstructor(DynamicClass<T> dClass, Object... dynamicableParams) {
		this.dClass = Objects.requireNonNull(dClass);
		this.params = DynamicClass.toDynamic(Objects.requireNonNull(dynamicableParams));
	}

	public boolean isCallable() {
		try {
			getC();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Constructor<T> getC() {
		if (c == null) {
			try {
				c = dClass.getC().getConstructor(DynamicClass.resolveAll(params));
			} catch (NoSuchMethodException e) {
				throw new TaskNotCompletedException("Kein öffentlicher Konstruktor für " + dClass + " mit Parametern " + DynamicMethod.descParams(this.params)
						+ " gefunden.");
			}
		}
		return c;
	}

	public Object newInstance(Object... params) {
		try {
			return getC().newInstance(params);
		} catch (InstantiationException e) {
			throw new TaskNotCompletedException("Objekt der Klasse " + dClass + " konnte nicht erzeugt werden, ist die Klasse abstract?");
		} catch (IllegalAccessException e) {
			throw new TaskNotCompletedException("Objekt der Klasse " + dClass + " konnte nicht erzeugt werden, Zugriff auf Konstruktor nicht möglich");
		} catch (IllegalArgumentException e) {
			throw new TaskNotCompletedException("Konstruktor " + this + " konnte Parametertypen " + DynamicMethod.descArgs(params) + " nicht entgegennehmen");
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof RuntimeException)
				throw (RuntimeException) e.getTargetException();
			throw new RuntimeException(e.getTargetException());
		}
	}

	@Override
	public String toString() {
		return dClass.toString() + "(" + DynamicMethod.descParams(params) + ")";
	}
}
