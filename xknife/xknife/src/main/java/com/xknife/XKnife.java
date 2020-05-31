package com.xknife;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class XKnife {
    private static Map<Class<?>, Constructor<?>> BINDINGS = new HashMap<>();

    public static void bind(Activity target) {
        bind(target, target.getWindow().getDecorView());
    }

    public static void bind(Activity target, View source) {
        Objects.requireNonNull(target);
        Objects.requireNonNull(source);
        Constructor<?> constructor = findConstructorForClass(target.getClass());
        if(constructor != null){
            try {
                constructor.newInstance(target, source);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static Constructor<?> findConstructorForClass(Class<?> cls) {
        Constructor<?> constructor = BINDINGS.get(cls);
        if (constructor != null) {
            return constructor;
        }
        try {
            Class<?> viewBindingCls = Class.forName(cls.getSimpleName() + "_ViewBinding");
            constructor = viewBindingCls.getConstructor(Activity.class, View.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        BINDINGS.put(cls, constructor);
        return constructor;
    }

}
