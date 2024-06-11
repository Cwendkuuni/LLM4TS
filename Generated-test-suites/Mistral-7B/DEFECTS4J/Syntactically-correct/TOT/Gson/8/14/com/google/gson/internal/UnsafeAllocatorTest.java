import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RunWith(Parameterized.class)
@Category(IntegrationTests.class)
public class UnsafeAllocatorTest {

  private Class<UnsafeAllocator> clazz;
  private UnsafeAllocator unsafeAllocator;

  public UnsafeAllocatorTest(Class<UnsafeAllocator> clazz) {
    this.clazz = clazz;
  }

  @Parameters
  public static Iterable<Class<UnsafeAllocator>> getTestCases() {
    try {
      return new Iterable<Class<UnsafeAllocator>>() {
        @Override
        public Iterator<Class<UnsafeAllocator>> iterator() {
          return new Iterator<Class<UnsafeAllocator>>() {
            boolean hasNext = true;

            @Override
            public boolean hasNext() {
              return hasNext;
            }

            @Override
            public Class<UnsafeAllocator> next() {
              try {
                return (Class<UnsafeAllocator>) Class.forName(
                    "com.google.gson.internal.UnsafeAllocator");
              } catch (ClassNotFoundException e) {
                hasNext = false;
                throw new RuntimeException(e);
              }
            }
          };
        }
      };
    } catch (Exception e) {
      throw new RuntimeException("Failed to load UnsafeAllocator class", e);
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testNewInstance() throws Exception {
    unsafeAllocator = this.clazz.getDeclaredConstructor().newInstance();

    Object obj = unsafeAllocator.newInstance(String.class);
    assertNotNull(obj);
    assertEquals("", (String) obj);

    obj = unsafeAllocator.newInstance(Serializable.class);
    assertNotNull(obj);

    obj = unsafeAllocator.newInstance(Integer[].class);
    assertNotNull(obj);
    Integer[] arr = (Integer[]) obj;
    assertEquals(0, arr.length);

    obj = unsafeAllocator.newInstance(MyClass.class);
    assertNotNull(obj);
    assertTrue(obj instanceof MyClass);

    try {
      obj = unsafeAllocator.newInstance(AbstractClass.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceWithInterface() throws Exception {
    unsafeAllocator = this.clazz.getDeclaredConstructor().newInstance();

    try {
      unsafeAllocator.newInstance(MyInterface.class);
      fail("Expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  @Test
  public void testSerializableObject() throws Exception {
    UnsafeAllocator unsafeAllocator = this.clazz.getDeclaredConstructor().newInstance();
    MySerializableObject obj = new MySerializableObject();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(obj);
    oos.close();

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);

    MySerializableObject deserializedObj = (MySerializableObject) ois.readObject();
    assertEquals(obj, deserializedObj);

    Object deserializedObjWithUnsafeAllocator = unsafeAllocator.newInstance(MySerializableObject.class);
    assertEquals(obj, deserializedObjWithUnsafeAllocator);
  }

  private static class MyClass {
  }

  private static abstract class AbstractClass {
  }

  private static interface MyInterface {
  }

  private static class MySerializableObject implements Serializable {
  }
}