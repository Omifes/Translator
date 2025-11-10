// Runner.java
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class Runner {
    public static void main(String[] args) throws Exception {
        Service service = new Service();

        for (Method m : Service.class.getDeclaredMethods()) {
            if (!m.isAnnotationPresent(Repeat.class)) continue;

            int mod = m.getModifiers();
            if (Modifier.isProtected(mod) || Modifier.isPrivate(mod)) {
                m.setAccessible(true);
                Repeat rep = m.getAnnotation(Repeat.class);
                Object[] callArgs = defaultsFor(m.getParameterTypes());

                for (int i = 0; i < rep.value(); i++) {
                    m.invoke(service, callArgs);
                }
            }
        }
    }

    private static Object[] defaultsFor(Class<?>[] types) {
        Object[] args = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            args[i] = defaultValue(types[i]);
        }
        return args;
    }

    private static Object defaultValue(Class<?> t) {
        if (t.isArray()) {
            return Array.newInstance(t.getComponentType(), 0);
        }
        if (!t.isPrimitive()) return null;

        if (t == boolean.class) return false;
        if (t == byte.class)    return (byte) 0;
        if (t == short.class)   return (short) 0;
        if (t == int.class)     return 0;
        if (t == long.class)    return 0L;
        if (t == float.class)   return 0f;
        if (t == double.class)  return 0d;
        if (t == char.class)    return '\0';
        return null;
    }
}
