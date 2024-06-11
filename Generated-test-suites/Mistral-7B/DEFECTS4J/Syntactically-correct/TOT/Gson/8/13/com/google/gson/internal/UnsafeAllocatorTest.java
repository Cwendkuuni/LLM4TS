import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
@Category(IntegrationTest.class)
public class UnsafeAllocatorTest {

  private final Class<?> clazz;
  private final UnsafeAllocator allocator;

  public UnsafeAllocatorTest(Class<?> clazz) {
    this.clazz = clazz;
    this.allocator = UnsafeAllocator.create();
  }

  @Parameters
  public static Collection<Class<?>> data() {
    return Arrays.asList(String.class, Integer.class, List.class, MySerializableClass.class);
  }

  @Test
  public void testNewInstance() throws Exception {
    Object instance = allocator.newInstance(clazz);
    assertInstanceOf(clazz, instance);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testNewInstanceFail() throws Exception {
    try {
      allocator.newInstance(Object[].class);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
  public void testSerializableClass() throws Exception {
    MySerializableClass obj = (MySerializableClass) allocator.newInstance(MySerializableClass.class);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(outputStream);
    out.writeObject(obj);

    ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    ObjectInputStream in = new ObjectInputStream(inputStream);

    MySerializableClass deserializedObj = (MySerializableClass) in.readObject();
    assertInstanceOf(MySerializableClass.class, deserializedObj);
  }

  @Test(expected = ClassCastException.class)
  public void testClassCastException() throws Exception {
    try {
      allocator.newInstance(String[].class);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test(expected = InstantiationException.class)
  public void testInstantiationException() throws Exception {
    try {
      allocator.newInstance(Exception.class);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
  public void testReflectiveAccess() throws Exception {
    Method method = clazz.getDeclaredMethod("someMethod");
    method.setAccessible(true);
    Object instance = allocator.newInstance(clazz);
    method.invoke(instance);
  }

  @Test(expected = IllegalAccessException.class)
  public void testIllegalAccessException() throws Exception {
    Method method = Object[].class.getDeclaredMethod("length");
    method.setAccessible(true);

    Object array = allocator.newInstance(Object[].class);
    method.invoke(array);
  }

  @Test
  public void testConstructorInvoke() throws Exception {
    Class<?> constructorClass = MySerializableClassWithConstructor.class;
    Object instance = allocator.newInstance(constructorClass);
    assertInstanceOf(MySerializableClassWithConstructor.class, instance);
  }

  private static class MySerializableClass implements Serializable {
  }

  private static class MySerializableClassWithConstructor implements Serializable {
    public MySerializableClassWithConstructor() {
    }
  }
}