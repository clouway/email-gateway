package com.clouway.telcong.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Set;

/**
 * Announcer class is a helper class which is used to announce different kind of message to one or several listeners that
 * are attached to the current announcer.
 *
 * @author Miroslav Genov (mgenov@gmail.com)
 */
public final class Announcer<T extends EventListener> {

  public static <T extends EventListener> Announcer<T> to(Class<? extends T> listenerType) {
    return new Announcer<T>(listenerType);
  }

  public static <T extends EventListener> Announcer<T> to(Class<? extends T> listenerType, Set<T> listeners) {
    Announcer<T> annoncer = new Announcer<T>(listenerType);
    for (T listener : listeners) {
      annoncer.addListener(listener);
    }
    return annoncer;
  }


  private final T proxy;

  private final List<T> listeners = new ArrayList<T>();

  Announcer(Class<? extends T> listenerType) {
    proxy = listenerType.cast(Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, new InvocationHandler() {
      public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        announce(method, objects);
        return null;
      }

    }));
  }

  public void addListener(T listener) {
    listeners.add(listener);
  }

  public T announce() {
    return proxy;
  }

  private void announce(Method method, Object[] objects) throws Throwable {
    for (T listener : listeners) {
      try {
        method.invoke(listener, objects);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        throw e.getTargetException();
      }
    }
  }
}
