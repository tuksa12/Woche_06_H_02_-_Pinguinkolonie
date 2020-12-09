package pgdp.helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DynamicField<T> {

	final DynamicClass<?> dClass;
	final List<String> fName;
	final DynamicClass<T> fType;
	final DynamicMethod<T> backup;
	final boolean ignoreCase;
	Class<?> c;
	Field f;

	public DynamicField(DynamicClass<?> dClass, Class<T> fType, DynamicMethod<T> backup, boolean ignoreCase,
			String... fNames) {
		this(dClass, DynamicClass.toDynamic(fType), backup, ignoreCase, fNames);
	}

	public DynamicField(DynamicClass<?> dClass, DynamicClass<T> fType, DynamicMethod<T> backup, boolean ignoreCase,
			String... fNames) {
		this.dClass = Objects.requireNonNull(dClass);
		this.ignoreCase = ignoreCase;
		this.fName = List.of(Objects.requireNonNull(fNames));
		this.fType = Objects.requireNonNull(fType);
		this.backup = backup;
	}

	public Field getF() {
		if (f == null) {
			var of = findField(dClass.getC());
			if (of.isPresent()) {
				f = of.get();
				f.setAccessible(true);
			} else
				throw new TaskNotCompletedException("Feld " + fName + " konnte nicht gefunden/geraten werden");
		}
		return f;
	}

	@SuppressWarnings("unchecked")
	public T getOf(Object o) {
		try {
			return (T) getF().get(o);
		} catch (IllegalAccessException e) {
			throw new TaskNotCompletedException("Feld " + fName + " der Klasse " + dClass
					+ " konnte nicht aufgerufen werden, Zugriff auf das Feld nicht möglich");
		} catch (IllegalArgumentException e) {
			throw new TaskNotCompletedException("Feld " + fName + " von Klasse " + dClass
					+ " wurde nicht auf einem passenden Objekt aufgerufen (-> Testfehler)");
		} catch (ClassCastException e) {
			throw new TaskNotCompletedException("Feld " + fName + " der Klasse " + dClass + " kann nicht nach " + fType.getName() + "gecastet werden");
		}
	}

	public T getStatic() {
		try {
			return getOf(null);
		} catch (NullPointerException e) {
			throw new TaskNotCompletedException("Feld " + fName + " der Klasse " + dClass + " ist nicht statisch");
		}
	}

	public Optional<Field> findField(Class<?> c) {
		return fieldsOf(c).stream().filter(f -> fName.contains(f.getName())).findFirst();
	}

	public List<Field> fieldsOf(Class<?> c) {
		ArrayList<Field> al = new ArrayList<>();
		while (c != Object.class) {
			for (Field ff : c.getDeclaredFields())
				if (fType.getC().isAssignableFrom(ff.getType()))
					al.add(ff);
			c = c.getSuperclass();
		}
		return al;
	}
}
