import com.google.gson.TypeInfoFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new TypeToken<int[]>() {}.getType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(typeInfo);
    assertEquals(int[].class, typeInfo.getType());
    assertEquals("int[]", typeInfo.toString());
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException {
    TestClass testClass = new TestClass();
    Field field = TestClass.class.getDeclaredField("field");
    Type type = field.getGenericType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, type.getClass());
    assertNotNull(typeInfo);
    assertEquals(int.class, typeInfo.getType());
    assertEquals("int", typeInfo.toString());
  }

  @Test
  public void testGetActualTypeForClass() {
    Type type = Integer.TYPE;
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(null, type, null);
    assertEquals(type, typeInfo.getType());
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    TestClass testClass = new TestClass();
    Type type = new TypeToken<List<String>>() {}.getType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(
        testClass.getClass().getDeclaredField("list"), type, testClass.getClass());
    assertNotNull(typeInfo);
    assertEquals(List.class, typeInfo.getType());
    assertEquals("java.util.List<java.lang.String>", typeInfo.toString());
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    Type type = new TypeToken<int[][]>() {}.getType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertNotNull(typeInfo);
    assertEquals(int[][].class, typeInfo.getType());
    assertEquals("int[][]", typeInfo.toString());
  }

  @Test
  public void testGetActualTypeForTypeVariable() throws NoSuchFieldException {
    TestClass testClass = new TestClass();
    Field field = TestClass.class.getDeclaredField("variableA");
    Type type = field.getGenericType();
    int index = Arrays.asList(testClass.getClass().getTypeParameters())
        .indexOf(new TypeVariable<>("A") {}.getType());
    Type actualType = TypeUtils.getRawType(testClass.getClass().getGenericSuperclass())
        .getActualTypeArguments()[index];
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, type, testClass.getClass());
    assertNotNull(typeInfo);
    assertEquals(actualType, typeInfo.getType());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForUnknownType() {
    Type type = new Object() {}.getClass().getGenericSuperclass();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(null, type, null);
    fail("Unexpected result: " + typeInfo);
  }

  private static class TestClass {
    int field;
    List<String> list;

    @SuppressWarnings("rawtypes")
    static class List extends ArrayList {}

    interface Interface<A> {}

    static class NestedClass<A> {}

    static class NestedInterface<B> {}

    static class SubClass<A> extends TestClass {}

    TypeVariable<Integer> variableA = new TypeVariable<Integer>("A") {};
  }
}