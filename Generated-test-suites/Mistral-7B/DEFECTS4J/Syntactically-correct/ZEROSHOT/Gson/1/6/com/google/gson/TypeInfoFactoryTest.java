import static org.junit.Assert.*;
import static com.google.common.reflect.Types.getRawType;
import static com.google.common.reflect.Types.newParameterizedType;
import static com.google.common.reflect.Types.newTypeVariable;
import static com.google.common.reflect.Types.wildcardTypeOf;

import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeInfo;
import com.google.gson.reflect.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() throws Exception {
    Type type = newTypeVariable(int.class, "T");
    TypeInfo expected = new TypeInfoArray(TypeToken.get(int[].class).getType());

    TypeInfo actual = TypeInfoFactory.getTypeInfoForArray(type);

    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField() throws Exception {
    Class<?> clazz = Integer[].class;
    Field field = clazz.getDeclaredField("length");
    Type type = field.getGenericType();

    Type parentType = getRawType(clazz);
    TypeInfo expected = new TypeInfo(Integer.TYPE);

    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, type, parentType);

    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualTypeForClass() throws Exception {
    Class<?> clazz = Integer[].class;
    Type type = clazz;

    Type expected = Integer[].class;
    TypeInfo actual = TypeInfoFactory.getActualType(type, null, clazz);

    assertEquals(expected, actual.getType());
  }

  @Test
  public void testGetActualTypeForParameterizedType() throws Exception {
    Class<?> clazz = new ArrayList<Integer>().getClass();
    Field elementTypeField = clazz.getDeclaredField("class");
    Type type = elementTypeField.getGenericType();

    Type parentType = getRawType(clazz);
    Class<?> rawParentClass = (Class<?>) parentType;

    Type expected = Integer.TYPE;
    TypeInfo actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);

    assertEquals(expected, actual.getType());
  }

  @Test
  public void testGetActualTypeForGenericArrayType() throws Exception {
    Class<?> clazz = Integer[].class;
    Type type = TypeToken.get(clazz).getType();

    Type parentType = getRawType(clazz);
    Class<?> rawParentClass = (Class<?>) parentType;

    Type expected = Integer.TYPE;
    TypeInfo actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);

    assertEquals(expected, actual.getType());
  }

  @Test
  public void testGetActualTypeForTypeVariable() throws Exception {
    Class<?> clazz = new ArrayList<Integer>().getClass();
    Field elementTypeField = clazz.getDeclaredField("E");
    Type type = elementTypeField.getGenericType();

    Type parentType = getRawType(clazz);
    Class<?> rawParentClass = (Class<?>) parentType;

    TypeVariable<Integer> fieldTypeVariable = (TypeVariable<Integer>) type;

    int indexOfActualTypeArgument = 0;
    Type[] actualTypeArguments = ((ParameterizedType) parentType).getActualTypeArguments();
    Type actual = actualTypeArguments[indexOfActualTypeArgument];

    Type expected = Integer.TYPE;
    TypeInfo actualInfo = new TypeInfo(actual);
    TypeInfo expectedInfo = new TypeInfo(expected);

    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(elementTypeField, type, parentType);

    assertEquals(expectedInfo, actualTypeInfo);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForWildcardType() throws Exception {
    Class<?> clazz = new ArrayList<Object>().getClass();
    Field elementTypeField = clazz.getDeclaredField("E");
    Type type = elementTypeField.getGenericType();

    Type parentType = getRawType(clazz);
    Class<?> rawParentClass = (Class<?>) parentType;

    Type expected = TypeToken.get(List.class).getType();
    TypeInfo actual = TypeInfoFactory.getActualType(type, parentType, rawParentClass);

    fail("Should have thrown UnsupportedOperationException");
  }
}