 package com.google.gson.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Excluder;
import com.google.gson.Preconditions;
import com.google.gson.TypeUtils;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ArrayMap;
import com.google.gson.internal.ArrayType;
import com.google.gson.internal.ParameterizedTypeImpl;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.TypeVariableImpl;
import com.google.gson.internal.Types;
import com.google.gson.internal.WildcardTypeImpl;
import com.google.gson.stream.TypeToken;

import static com.google.gson.stream.TypeTokens.*;

public class TypeInfoFactoryTest {

  private static Class<?> clazz;
  private static Field field;

  @BeforeClass
  public static void setupClass() throws Exception {
    clazz = Map.class.getParameterizedType(String.class, List.class);
    field = clazz.getDeclaredField("Key");
  }

  @Test public void testTypeInfoForArray() {
    Type intArrayType = new ArrayType(int.class, 1);
    TypeInfo expectedTypeInfo = new TypeInfoArray(intArrayType);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(intArrayType);
    assertEquals(expectedTypeInfo, actualTypeInfo);
  }

  @Test public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    Type expectedTypeInfoType = new TypeToken<String>() {}.getType();
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, clazz.getGenericSuperclass());
    assertEquals(expectedTypeInfoType, actualTypeInfo.getType());
  }

  @Test public void testGetActualType_ClassType() {
    Class<?> clazz = String.class;
    Type expectedType = clazz;
    Type actualType = TypeInfoFactory.getActualType(expectedType, null, null);
    assertEquals(expectedType, actualType);
  }

  @Test public void testGetActualType_ParameterizedType() {
    TypeList<String> listType = new TypeToken<TypeList<String>>() {}.getType();
    TypeParameterizedType parameterizedType = (TypeParameterizedType) listType.getRawType();
    Class<?> rawParentClass = ArrayList.class;
    Type[] expectedTypes = new Type[] { String.class };
    Type[] actualTypes = TypeInfoFactory.getActualTypes(parameterizedType, null, rawParentClass);
    assertArrayEquals(expectedTypes, actualTypes);
  }

  @Test public void testGetActualType_GenericArrayType() {
    Class<String[]> stringArrayClass = String[].class;
    Type expectedType = stringArrayClass;
    Type actualType = TypeInfoFactory.getActualType(new GenericArrayTypeImpl(String.class), null, null);
    assertEquals(expectedType, actualType);
  }

  @Test public void testGetActualType_TypeVariable() {
    TypeVariable<Class<Map<String, List<String>>>> keyTypeVariable = (TypeVariable<Class<Map<String, List<String>>>>) clazz.getTypeParameters()[0];
    Class<?> rawParentClass = ArrayList.class;
    Type expectedType = ArrayList.class;
    Type actualType = TypeInfoFactory.getActualType(keyTypeVariable, null, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test public void testGetActualType_WildcardType() {
    TypeList<String> listType = new TypeToken<TypeList<String>>() {}.getType();
    WildcardType wildcardType = (WildcardType) listType.getComponentType();
    Class<?> rawParentClass = ArrayList.class;
    Type expectedType = Collection.class;
    Type actualType = TypeInfoFactory.getActualType(wildcardType, null, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForField_FieldIsNotStatic() throws NoSuchFieldException, IllegalAccessException {
    Class<String> clazz = String.class;
    Field field = clazz.getDeclaredField("value");
    Type typeDefiningF = clazz.getSuperclass();
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testTypeInfoForField_TypeVariableDoesNotExistInParentType() throws NoSuchFieldException, IllegalAccessException {
    Class<Map<String, List<String>>> mapClass = Map.class.getParameterizedType(String.class, List.class);
    Field keyTypeVariable = mapClass.getDeclaredField("Key");
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(keyTypeVariable, mapClass);
  }

  @Test public void testGetActualTypes_NullParentType() {
    TypeList<String> listType = new TypeToken<TypeList<String>>() {}.getType();
    TypeParameterizedType parameterizedType = (TypeParameterizedType) listType.getRawType();
    Type[] expectedTypes = new Type[] { String.class };
    Type[] actualTypes = TypeInfoFactory.getActualTypes(parameterizedType, null, null);
    assertArrayEquals(expectedTypes, actualTypes);
  }

  @Test public void testGetActualTypes_NullRawParentClass() {
    TypeList<String> listType = new TypeToken<TypeList<String>>() {}.getType();
    TypeParameterizedType parameterizedType = (TypeParameterizedType) listType.getRawType();
    Type[] expectedTypes = new Type[] { String.class };
    Type[] actualTypes = TypeInfoFactory.getActualTypes(parameterizedType, null, null);
    assertArrayEquals(expectedTypes, actualTypes);
  }

  @Test public void testGetActualTypes_EmptyParameterizedType() {
    TypeParameterizedType emptyParameterizedType = new ParameterizedTypeImpl(Object.class, new Type[] {}, null);
    Type expectedTypes[] = new Type[0];
    Type actualTypes[] = TypeInfoFactory.getActualTypes(emptyParameterizedType, null, null);
    assertArrayEquals(expectedTypes, actualTypes);
  }

  @Test public void testGetActualTypes_EmptyRawParentClass() {
    TypeParameterizedType emptyParameterizedType = new ParameterizedTypeImpl(Object.class, new Type[] {}, null);
    Type actualTypes[] = TypeInfoFactory.getActualTypes(emptyParameterizedType, null, null);
    assertArrayEquals(new Type[0], actualTypes);
  }

  @Test public void testGetActualTypes_ParameterizedTypesWithWildCard() {
    Type listType = new TypeToken<List<String>>() {}.getType();
    TypeParameterizedType parameterizedType = (TypeParameterizedType) listType.getRawType();
    Class<?> rawParentClass = ArrayList.class;
    Type[] expectedTypes = new Type[] { Collection.class, String.class };
    Type[] actualTypes = TypeInfoFactory.getActualTypes(parameterizedType, null, rawParentClass);
    assertArrayEquals(expectedTypes, actualTypes);
  }

  @Test public void testGetActualTypes_ClassWithTypeVariables() {
    Type mapType = mapType(String.class, List.class);
    TypeVariable<?> keyTypeVariable = mapType.getTypeParameters()[0];
    TypeVariable<?> valueTypeVariable = mapType.getTypeParameters()[1];
    Class<?> rawParentClass = HashMap.class;
    Type expectedTypes[] = new Type[] { Map.class, String.class, List.class };
    Type[] actualTypes = TypeInfoFactory.getActualTypes(mapType, null, rawParentClass);
    assertArrayEquals(expectedTypes, actualTypes);
  }

  // ... Add more test cases for other methods in TypeInfoFactory

  private static Type mapType(Class<?> keyType, Class<?> valueType) {
    Class<?> mapClass = Map.class.getParameterizedType(keyType, valueType);
    return mapClass;
  }
}
