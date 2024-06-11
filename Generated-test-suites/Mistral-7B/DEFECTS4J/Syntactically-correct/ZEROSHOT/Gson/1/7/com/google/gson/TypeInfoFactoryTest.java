package com.google.gson. tests;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoFactory;
import com.google.gson.TypeVariableImpl;
import com.google.gson.TypeUtils;
import com.google.gson.internal.ParameterizedTypeImpl;
import com.google.gson.internal.PrimitiveTypeAdapter.WrapperType;
import com.google.gson.internal.TypeUtils;

/**
 * Tests for TypeInfoFactory class.
 */
public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = int[].class.getComponentType();
    TypeInfoArray expectedTypeInfo = new TypeInfoArray(type);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException {
    Class<Person> personClass = Person.class;
    Field field = Person.class.getField("name");
    Type expectedType = String.class;
    Type definingType = personClass;
    Type type = field.getGenericType();
    Type actualType = TypeInfoFactory.getActualType(type, definingType, personClass);
    TypeInfo expectedTypeInfo = new TypeInfo(expectedType);
    TypeInfo actualTypeInfo = TypeInfoFactory.getTypeInfoForField(field, definingType);
    assertEquals(expectedTypeInfo.getType(), actualTypeInfo.getType());
  }

  @Test
  public void testGetActualTypeForClass() {
    Class<Person> expectedType = Person.class;
    Type type = Person.class;
    Type parentType = Object.class;
    Type actualType = TypeInfoFactory.getActualType(type, parentType, null);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    Class<PersonList<String>> expectedType = PersonList.class;
    Type typeToEvaluate = new ParameterizedTypeImpl(List.class, String.class);
    Type parentType = Object.class;
    Type rawParentClass = Object.class;
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    Class<String[]> expectedType = String[].class;
    Type typeToEvaluate = new GenericArrayTypeImpl(String.class);
    Type parentType = Object.class;
    Type rawParentClass = Object.class;
    Type actualType = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForTypeVariable() {
    Class<Person> personClass = Person.class;
    TypeVariable<String> stringTypeVariable = TypeVariable.class.getDeclaredField("value");
    stringTypeVariable.setAccessible(true);
    String expectedTypeString = "T";
    TypeVariable<String> fieldTypeVariable = new TypeVariableImpl<>("T");
    TypeVariable<?>[] classTypeVariables = personClass.getTypeParameters();
    ParameterizedType objParameterizedType = new ParameterizedTypeImpl(personClass, new Type[]{});
    int indexOfActualTypeArgument = 0;
    Type[] actualTypeArguments = new Type[]{String.class};
    Type expectedType = String.class;
    Type actualType = TypeInfoFactory.getActualType(fieldTypeVariable, objParameterizedType,
        personClass);
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetActualTypeForWildcardType() {
    Class<PersonList<?>> personListType = PersonList.class;
    Type wildcardType = new WildcardTypeImpl(Object.class, Object.class);
    Type parentType = Object.class;
    Type rawParentClass = Object.class;
    Type actualType = TypeInfoFactory.getActualType(wildcardType, parentType, rawParentClass);
    assertNull(actualType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualTypeForInvalidType() {
    Class<Person> personClass = Person.class;
    Type typeToEvaluate = new TypeVariableImpl<>("T");
    Type parentType = Object.class;
    Type rawParentClass = Object.class;
    TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
  }

  @Test
  public void testExtractRealTypes() {
    Type[] expectedTypes = new Type[]{String.class, Integer.class};
    Type[] actualTypes = TypeInfoFactory.extractRealTypes(
        new ParameterizedType[]{new ParameterizedTypeImpl(List.class, new Type[]{String.class, Integer.class})},
        Object.class, Object.class);
    assertEquals(expectedTypes, actualTypes);
  }

  @Test
  public void testGetIndex() {
    TypeVariable<String> expectedTypeVariable = new TypeVariableImpl<>("T");
    TypeVariable<?>[] types = new TypeVariable[]{new TypeVariableImpl<>("X"), expectedTypeVariable};
    int expectedIndex = 1;
    int actualIndex = TypeInfoFactory.getIndex(types, expectedTypeVariable);
    assertEquals(expectedIndex, actualIndex);
  }

  private static class PersonList<T> {
  }

  private static class Person {
    String name;
  }
}