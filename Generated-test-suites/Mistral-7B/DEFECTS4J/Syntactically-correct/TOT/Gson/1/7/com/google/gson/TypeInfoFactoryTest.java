package com.google.gson.tests;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.junit.Test;

import com.google.gson.TypeInfoFactory;
import com.google.gson.internal.Preconditions;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[5].getClass().getComponentType();
    TypeInfoTypeInfoArray info = TypeInfoFactory.TypeInfoForArray.getTypeInfoForArray(type);
    assertThat(info.getType()).isInstanceOf(int[].class);
  }

  @Test
  public void testTypeInfoForField_simple() throws Exception {
    Class<?> clazz = Integer.class;
    Field f = clazz.getDeclaredField("MIN_VALUE");
    Type infoType = TypeInfoFactory.getTypeInfoForField(f, clazz.getGenericSuperclass());
    assertThat(infoType.getType()).isInstanceOf(int.class);
  }

  @Test
  public void testTypeInfoForField_parameterized() throws Exception {
    Class<?> clazz = Map.class;
    Field f = clazz.getDeclaredField("entrySet");
    Type type = f.getGenericType();
    Type infoType = TypeInfoFactory.getTypeInfoForField(f, clazz.getGenericSuperclass());
    Type expectedType = new TypeToken<Collection<Map.Entry<String, String>>>() {}.getType();
    assertThat(infoType.getType()).isEqualTo(expectedType);
  }

  @Test
  public void testTypeInfoForField_wildcard() throws Exception {
    Class<?> clazz = ArrayList.class;
    Field f = clazz.getDeclaredField("list");
    Type infoType = TypeInfoFactory.getTypeInfoForField(f, clazz.getGenericSuperclass());
    Type expectedType = new TypeToken<List<?>>() {}.getType();
    assertThat(infoType.getType()).isEqualTo(expectedType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_unsupported() throws Exception {
    Class<?> clazz = String.class;
    Field f = clazz.getDeclaredField("length"); // String does not have a length field
    Type infoType = TypeInfoFactory.getTypeInfoForField(f, clazz.getGenericSuperclass());
  }

  @Test
  public void testGetActualType_simpleType() {
    Type type = Integer.TYPE;
    Type actualType = TypeInfoFactory.getActualType(type, null, null);
    assertThat(actualType).isEqualTo(type);
  }

  @Test
  public void testGetActualType_parameterizedType() {
    Type parentType = new TypeToken<Map<String, List<String>>>() {}.getType();
    Type type = new TypeToken<Map.Entry<String, List<String>>>() {}.getType();
    Class<?> rawParentClass = Map.class;
    Type actualType = TypeInfoFactory.getActualType(type, parentType, rawParentClass);
    Type expectedType = new TypeToken<Map.Entry<String, List<String>>>() {}.getType();
    assertThat(actualType).isEqualTo(expectedType);
  }

  @Test
  public void testGetActualType_genericArrayType() {
    Type parentType = new TypeToken<List<String>>() {}.getType();
    Type componentType = Integer.TYPE;
    Type genericArrayType = new TypeToken<Integer[]>() {}.getType();
    Class<?> rawParentClass = List.class;
    Type actualType = TypeInfoFactory.getActualType(genericArrayType, parentType, rawParentClass);
    Type expectedType = new TypeToken<Integer[]>() {}.getType();
    assertThat(actualType).isEqualTo(expectedType);
  }

  @Test
  public void testGetActualType_typeVariable() {
    Type parentType = new TypeToken<Map<String, List<String>>>() {}.getType();
    TypeVariable<String> typeVariable = new TypeVariable<>("X") {};
    Class<?> rawParentClass = Map.class;
    Type actualType = TypeInfoFactory.getActualType(typeVariable, parentType, rawParentClass);
    Type expectedType = String.class;
    assertThat(actualType).isEqualTo(expectedType);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualType_unsupported() {
    Type parentType = new TypeToken<List<String>>() {}.getType();
    Type type = new TypeToken<StringBuilder>() {}.getType();
    Class<?> rawParentClass = List.class;
    TypeInfoFactory.getActualType(type, parentType, rawParentClass);
  }

  @Test
  public void testExtractRealTypes_simpleTypes() {
    Type parentType = new TypeToken<Map<String, List<String>>>() {}.getType();
    Type[] actualTypeArguments = new Type[] { Integer.TYPE, String.class };
    Type[] expectedTypeArguments = new Type[] { Integer.TYPE, String.class };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, null);
    assertThat(actualTypes).isEqualTo(expectedTypeArguments);
  }

  @Test
  public void testExtractRealTypes_parameterizedTypes() {
    Type parentType = new TypeToken<Map<String, Map<String, List<String>>>>() {}.getType();
    Type[] actualTypeArguments = new Type[] { new TypeToken<String>() {}.getType(),
        new TypeToken<Map<String, List<String>>>() {}.getType() };
    Type[] expectedTypeArguments = new Type[] { String.class, Map.class };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, null);
    assertThat(actualTypes).isEqualTo(expectedTypeArguments);
  }

  @Test
  public void testExtractRealTypes_wildcardTypes() {
    Type parentType = new TypeToken<Map<String, List<?>>>() {}.getType();
    Type[] actualTypeArguments = new Type[] { new TypeToken<String>() {}.getType(),
        new WildcardType(null, List.class) };
    Type[] expectedTypeArguments = new Type[] { String.class, List.class };
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, null);
    assertThat(actualTypes).isEqualTo(expectedTypeArguments);
  }

  @Test
  public void testExtractRealTypes_mismatchedTypes() {
    Type parentType = new TypeToken<Map<String, List<Integer>>>() {}.getType();
    Type[] actualTypeArguments = new Type[] { new TypeToken<String>() {}.getType(),
        new TypeToken<List<String>>() {}.getType() };
    assertThrows(IllegalArgumentException.class, () -> TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, null));
  }

  @Test
  public void testGetIndex() {
    TypeVariable<String> typeVariable = new TypeVariable<>("X") {};
    TypeVariable<?>[] types = new TypeVariable[] { new TypeVariable<>("A") {}, typeVariable, new TypeVariable<>("Z") {} };
    int index = TypeInfoFactory.getIndex(types, typeVariable);
    assertThat(index).isEqualTo(1);
  }
}