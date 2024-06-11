package com.example.unsafeallocator;

import com.google.gson.internal.UnsafeAllocator;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UnsafeAllocatorTest {

  private static final UnsafeAllocator UNSAFE_ALLOCATOR = UnsafeAllocator.create();

  @Test
  public void testNewInstanceWithNormalClass() throws Exception {
    // Test with a normal class
    Class<Integer> intType = Integer.TYPE;
    Integer intObject = UNSAFE_ALLOCATOR.newInstance(intType);
    assertNotNull(intObject);
    assertEquals(0, intObject.intValue());
  }

  @Test
  public void testNewInstanceWithSerializableClass() throws Exception {
    // Test with a Serializable class
    class TestSerializable implements Serializable {
      private static final long serialVersionUID = 1L;
      private int value = 42;
    }

    TestSerializable testObject = UNSAFE_ALLOCATOR.newInstance(TestSerializable.class);
    assertNotNull(testObject);
    assertEquals(42, testObject.value);

    // Test serialization and deserialization
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
    objectOutputStream.writeObject(testObject);
    objectOutputStream.close();

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
    TestSerializable deserializedObject = (TestSerializable) objectInputStream.readObject();
    objectInputStream.close();

    assertEquals(testObject.value, deserializedObject.value);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithInterface() throws Exception {
    // Test with an interface
    Class<MyInterface> interfaceType = MyInterface.class;
    UNSAFE_ALLOCATOR.newInstance(interfaceType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithAbstractClass() throws Exception {
    // Test with an abstract class
    Class<MyAbstractClass> abstractClass = MyAbstractClass.class;
    UNSAFE_ALLOCATOR.newInstance(abstractClass);
  }

  @Test
  public void testNewInstanceWithException() throws Exception {
    // Test with a class that throws an exception in its constructor
    class TestException extends Exception {
      public TestException(String message) {
        super(message);
      }
    }

    Class<TestException> exceptionType = TestException.class;
    try {
      UNSAFE_ALLOCATOR.newInstance(exceptionType);
      fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }

    // Test with a method that throws an exception
    class TestExceptionThrowingMethod {
      @SuppressWarnings("unused")
      public int methodWithException() throws TestException {
        throw new TestException("Test exception");
      }

      public int normalMethod() {
        return 42;
      }
    }

    TestExceptionThrowingMethod testObject = new TestExceptionThrowingMethod();

    try {
      Method method = testObject.getClass().getMethod("methodWithException");
      UNSAFE_ALLOCATOR.newInstance((Class<TestException>) method.getReturnType());
      fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    // Test with a field that holds an instance of an exception-throwing class
    class TestExceptionHolder {
      @SuppressWarnings("unused")
      TestException testException;
    }

    TestExceptionHolder testObject = new TestExceptionHolder();

    try {
      Field field = TestExceptionHolder.class.getDeclaredField("testException");
      field.setAccessible(true);
      UNSAFE_ALLOCATOR.newInstance(TestException.class);
      field.set(testObject, new TestException("Test exception"));
      TestException testException = (TestException) field.get(testObject);
      fail("Expected an UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}