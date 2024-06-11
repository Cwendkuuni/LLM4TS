package com.google.gson.internal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

  private UnsafeAllocator unsafeAllocator;

  @Test
  public void testInit() {
    unsafeAllocator = UnsafeAllocator.create();
    assertNotNull(unsafeAllocator);
  }

  @Test
  public void testNewInstance() throws Exception {
    Class<Object> objectClass = Object.class;
    Object instance = unsafeAllocator.newInstance(objectClass);
    assertNotNull(instance);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithInterface() throws Exception {
    Class<Runnable> runnableInterface = Runnable.class;
    UnsafeAllocator unsafeAllocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        return null;
      }
    };
    unsafeAllocator.newInstance(runnableInterface);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithAbstractClass() throws Exception {
    Class<AbstractClass> abstractClass = AbstractClass.class;
    UnsafeAllocator unsafeAllocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        return null;
      }
    };
    unsafeAllocator.newInstance(abstractClass);
  }

  @Test
  public void testNewInstanceWithConstructor() throws Exception {
    Class<WithPublicConstructor> withPublicConstructor = WithPublicConstructor.class;
    Object instance = unsafeAllocator.newInstance(withPublicConstructor);
    assertNotNull(instance);

    Constructor<WithPublicConstructor> constructor = withPublicConstructor.getDeclaredConstructor();
    WithPublicConstructor instanceViaConstructor = (WithPublicConstructor) constructor.newInstance();
    assertNotNull(instanceViaConstructor);

    assertNotSame(instance, instanceViaConstructor);
  }

  @Test(expected = InstantiationException.class)
  public void testNewInstanceWithNoPublicConstructor() throws Exception {
    Class<WithNoPublicConstructor> withNoPublicConstructor = WithNoPublicConstructor.class;
    unsafeAllocator.newInstance(withNoPublicConstructor);
  }

  @Test
  public void testNewInstanceWithNoPublicOrDefaultConstructor() throws Exception {
    Class<WithNoPublicNorDefaultConstructor> withNoPublicNorDefaultConstructor = WithNoPublicNorDefaultConstructor.class;
    Object instance = unsafeAllocator.newInstance(withNoPublicNorDefaultConstructor);
    assertNotNull(instance);
  }

  private static class AbstractClass {
  }

  private static class WithNoPublicConstructor {
    private WithNoPublicConstructor() {
    }
  }

  private static class WithNoPublicNorDefaultConstructor {
    private WithNoPublicNorDefaultConstructor(int i) {
    }
  }

  private static class WithPublicConstructor {
    public WithPublicConstructor() {
    }
  }
}