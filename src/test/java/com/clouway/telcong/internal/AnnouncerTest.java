package com.clouway.telcong.internal;

import org.junit.Test;

import java.util.EventListener;

import static junit.framework.TestCase.assertTrue;

/**
 * @author Vasil Mitov <vasil.mitov@clouway.com>
 */
public class AnnouncerTest {

  interface MyInterface extends EventListener {
    void doSomething();
  }


  class MyImpl1 implements MyInterface {

    private boolean executed = false;

    @Override
    public void doSomething() {
      System.out.println("impl1");
      executed = true;
    }
  }

  class MyImpl2 implements MyInterface {

    private boolean executed = false;

    @Override
    public void doSomething() {
      System.out.println("impl2");
      executed = true;
    }
  }

  class ExceptionImpl implements MyInterface {

    @Override
    public void doSomething() {
      throw new CustomException();
    }
  }

  class CustomException extends RuntimeException {
  }

  @Test()
  public void happyPath() {
    Announcer<MyInterface> announcer = Announcer.to(MyInterface.class);

    MyImpl1 firstListener = new MyImpl1();
    MyImpl2 secondListener = new MyImpl2();

    announcer.addListener(firstListener);
    announcer.addListener(secondListener);

    announcer.announce().doSomething();

    assertTrue("first implementation is not executed", firstListener.executed);
    assertTrue("second implementation is not executed", secondListener.executed);
  }

  @Test(expected = CustomException.class)
  public void throwingExceptions() throws Exception {
    Announcer<MyInterface> announcer = Announcer.to(MyInterface.class);

    ExceptionImpl exceptionListener = new ExceptionImpl();

    announcer.addListener(exceptionListener);

    announcer.announce().doSomething();

  }
}