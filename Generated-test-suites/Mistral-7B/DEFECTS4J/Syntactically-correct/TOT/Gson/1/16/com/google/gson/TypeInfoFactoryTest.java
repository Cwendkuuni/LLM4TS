import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new TypeToken<int[]>() {}.getType();
    TypeInfo expected = new TypeInfoArray(type);
    TypeInfo actual = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField_PrimitiveType() throws NoSuchFieldException {
    int[] arr = new int[3];
    Class<?> clazz = arr.getClass().getComponentType();
    Field f = clazz.getField("length");
    Type type = f.getGenericType();
    Type expecting = new TypeInfoInt();
    Type actual = TypeInfoFactory.getTypeInfoForField(f, TypeUtils.toRawClass(clazz), type).getType();
    assertEquals(expecting, actual);
  }

  @Test
  public void testTypeInfoForField_ClassType() throws NoSuchFieldException {
    String[] arr = new String[3];
    Class<?> clazz = arr.getClass();
    Field f = clazz.getField("length");
    Type type = f.getGenericType();
    Type expecting = new TypeInfoClassString();
    Type actual = TypeInfoFactory.getTypeInfoForField(f, TypeUtils.toRawClass(clazz), type).getType();
    assertEquals(expecting, actual);
  }

  @Test
  public void testTypeInfoForField_ParameterizedType() throws NoSuchFieldException {
    List<String> list = Arrays.asList("apple", "banana", "cherry");
    Type type = list.getClass().getGenericSuperclass();
    Field f = type.getDeclaredField("list");
    f.setAccessible(true);
    Type[] expectedElements = new Type[]{new TypeInfoClassString()};
    Type[] actualElements = TypeInfoFactory.getTypeInfoForField(f, type, type).getActualTypeArguments();
    assertArrayEquals(expectedElements, actualElements);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForField_UnknownType() throws NoSuchFieldException {
    int[] arr = new int[3];
    Class<?> clazz = arr.getClass().getComponentType();
    Field f = clazz.getField("asdf");
    Type type = f.getGenericType();
    TypeInfoFactory.getTypeInfoForField(f, TypeUtils.toRawClass(clazz), type);
  }

  @Test
  public void testGetActualType() {
    Type[] typeArguments = new Type[]{new TypeToken<String>() {}.getType(), new TypeToken<Integer>() {}.getType()};
    Type type = new TypeToken<Map<String, Integer>>() {}.getType();
    Type actual = TypeInfoFactory.getActualType(type, null, null);
    assertEquals(new ParameterizedTypeImpl(Map.class, typeArguments, null), actual);
  }

  @Test
  public void testGetActualType_ArrayType() {
    Type type = new TypeToken<int[]>() {}.getType();
    Type actual = TypeInfoFactory.getActualType(type, null, null);
    assertEquals(type, actual);
  }

  @Test
  public void testGetActualType_ParameterizedType() {
    Type type = new TypeToken<List<String>>() {}.getType();
    Type actual = TypeInfoFactory.getActualType(type, null, null);
    assertEquals(new ParameterizedTypeImpl(List.class, new Type[] {new TypeToken<String>() {}.getType()}, null), actual);
  }

  @Test
  public void testGetActualType_GenericArrayType() {
    Type type = new TypeToken<int[]>() {}.getType();
    Type actual = TypeInfoFactory.getActualType(type, null, null);
    assertEquals(new GenericArrayTypeImpl(new TypeToken<Integer>() {}.getType()), actual);
  }

  @Test
  public void testGetActualType_TypeVariable() {
    Type type = new TypeToken<Map<String, ?>>() {}.getType();
    Type actual = TypeInfoFactory.getActualType(type, null, null);
    assertEquals(new WildcardType(null, Arrays.asList(new TypeToken<String>() {}.getType(), null)), actual);
  }

  @Test
  public void testExtractRealTypes() {
    Type[] typeArguments = new Type[]{new TypeToken<String>() {}.getType(), new TypeToken<Integer>() {}.getType()};
    Type type = new TypeToken<Map<String, Integer>>() {}.getType();
    Type[] expected = new Type[]{new TypeToken<String>() {}.getType(), new TypeToken<Integer>() {}.getType()};
    Type[] actual = TypeInfoFactory.extractRealTypes(type.getActualTypeArguments(), type, null);
    assertArrayEquals(expected, actual);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<String> stringTypeVariable = new TypeVariable<String>("X") {};
    TypeVariable<Integer> integerTypeVariable = new TypeVariable<Integer>("Y") {};
    TypeVariable<?>[] types = new TypeVariable<?>[]{stringTypeVariable, integerTypeVariable};
    int index = TypeInfoFactory.getIndex(types, stringTypeVariable);
    assertEquals(0, index);
  }
}