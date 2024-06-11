package com.google.gson.testing;

import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

  @org.junit.Test
  public void testTypeInfoForArray() {
    Type type = Integer[].class.getGenericComponentType();
    TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(Integer.TYPE, typeInfoArray.getType());
  }

  @org.junit.Test
  public void testTypeInfoForField_withArrayType() throws NoSuchFieldException {
    Class<Integer[]> arrayClass = Integer[].class;
    Field field = arrayClass.getDeclaredField("value");
    field.setAccessible(true);
    Type arrayType = field.getGenericType();
    Class<?> rawDefiningClass = arrayClass;
    Type type = getActualType(arrayType, null, rawDefiningClass);
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
    assertEquals(type, typeInfo.getType());
  }

  @org.junit.Test
  public void testTypeInfoForField_withParameterizedType() throws NoSuchFieldException {
    class MyClass<T> {
      T field;
    }

    Field field = MyClass.class.getDeclaredField("field");
    field.setAccessible(true);
    Type type = field.getGenericType();
    Type rawDefiningClass = MyClass.class;
    Type actualType = getActualType(type, null, rawDefiningClass);
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
    assertEquals(actualType, typeInfo.getType());
  }

  @org.junit.Test
  public void testTypeInfoForField_withGenericArrayType() throws NoSuchFieldException {
    class MyClass<T> {
      T[] arrayField;
    }

    Field field = MyClass.class.getDeclaredField("arrayField");
    field.setAccessible(true);
    Type type = field.getGenericType();
    Class<?> rawDefiningClass = MyClass.class;
    Type actualType = getActualType(type, null, rawDefiningClass);
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
    assertEquals(actualType, typeInfo.getType());
  }

  @org.junit.Test
  public void testTypeInfoForField_withTypeVariable() throws NoSuchFieldException {
    class MyClass<A, B> {
      A field1;
      B field2;
    }

    Field field = MyClass.class.getDeclaredField("field1");
    field.setAccessible(true);
    Type type = field.getGenericType();
    Type rawDefiningClass = MyClass.class;
    Type actualType = getActualType(type, null, rawDefiningClass);
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
    assertEquals(TypeToken.getType(MyClass.class, 0), actualType);
  }

  @org.junit.Test
  public void testTypeInfoForField_withWildcardType() throws NoSuchFieldException {
    class MyClass<T> {
      List<T> listField;
    }

    Field field = MyClass.class.getDeclaredField("listField");
    field.setAccessible(true);
    Type type = field.getGenericType();
    Type rawDefiningClass = MyClass.class;
    Type actualType = getActualType(type, null, rawDefiningClass);
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, null);
    assertTrue(actualType instanceof WildcardType);
  }

  private Type getActualType(Type typeToEvaluate, Type parentType, Class<?> rawParentClass) {
    return TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
  }
}