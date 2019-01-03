package rocks.biankouski.runinfiregame.desktop.opengl.service;

import java.util.Hashtable;
import java.util.Map;

public class DI {

    private final Map<Class<?>, Initializable> callbacks = new Hashtable<>();
    private final Map<Class<?>, Object> initiated = new Hashtable<>();

    public interface Initializable<T> {
        T init(DI di) throws RuntimeException;
    }

    public <T> void share(Class<T> cls, Initializable<T> callback) {
        callbacks.put(cls, callback);
    }

    public <T> T get(Class<T> className) throws RuntimeException {
        Object instance = initiated.get(className);
        if (instance != null) {
            return className.cast(instance);
        }

        Initializable initializable = callbacks.get(className);
        if (initializable == null) {
            throw new RuntimeException(className.getName() + " wasn't declared.");
        }

        instance = initializable.init(this);
        initiated.put(className, instance);

        return className.cast(instance);
    }

}
