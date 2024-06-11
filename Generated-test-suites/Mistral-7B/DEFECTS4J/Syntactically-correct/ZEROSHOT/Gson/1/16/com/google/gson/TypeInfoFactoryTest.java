package com.google.gson.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.junit.Test;

import com.google.gson.exclusion.Preconditions;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeUtils;
import com.google.gson.reflect.TypeInfo;
import com.google.gson.reflect.TypeInfoArray;
import com.google.gson.reflect.TypeVariable;
import com.google.gson.reflect.WildcardTypeImpl;
import com.google.gson.reflect.ParameterizedTypeImpl;
import com.google.gson.reflect.GenericArrayTypeImpl;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new TypeToken<int[]>() {}.getType();
    TypeInfoArray info = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(TypeInfoArray.class, info.getClass());
    assertEquals(int[].class, info.getType());
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException, IllegalAccessException {
    TestClass testClass = new TestClass();
    Field field = TestClass.class.getField("intField");
    Type fieldType = field.getGenericType();

    Type parentType = TestClass.class;
    Type actualType = TypeInfoFactory.getActualType(fieldType, parentType, TestClass.class);

    TypeInfo info = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(TypeInfo.class, info.getClass());
    assertEquals(actualType, info.getType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTypeInfo() {
    Type invalidType = new Object() {}.getClass();
    TypeInfoFactory.getTypeInfoForField(new Field("invalidField", invalidType), TestClass.class);
  }

  @Test
  public void testGetActualType() {
    TestClass testClass = new TestClass();

    Type[] typeArguments = new Type[] { Integer.TYPE, String.class };
    Type parameterizedType = new TypeToken<TestClass<Integer, String>>() {}.getType();

    Type rawParentClass = TestClass.class;
    Type ownerType = new TypeToken<TestClass<Integer, String>>() {}.getRawType();

    Type actualType = TypeInfoFactory.getActualType(typeArguments[0], parameterizedType, rawParentClass);
    assertEquals(Integer.TYPE, actualType);

    actualType = TypeInfoFactory.getActualType(typeArguments[1], parameterizedType, rawParentClass);
    assertEquals(String.class, actualType);

    actualType = TypeInfoFactory.getActualType(parameterizedType, null, null);
    assertEquals(TestClass.class, actualType);

    actualType = TypeInfoFactory.getActualType(testClass.getClass(), null, null);
    assertEquals(TestClass.class, actualType);
  }

  @Test
  public void testExtractRealTypes() {
    TestClass<Integer, String> testClass = new TestClass<Integer, String>();
    Type parameterizedType = testClass.getClass();
    Type rawParentClass = TestClass.class;

    Type[] types = TypeInfoFactory.extractRealTypes(parameterizedType.getActualTypeArguments(), parameterizedType, rawParentClass);
    assertEquals(2, types.length);

    assertEquals(Integer.TYPE, types[0]);
    assertEquals(String.class, types[1]);
  }

  @Test
  public void testGetIndex() {
    TestClass<Integer, String> testClass = new TestClass<Integer, String>();
    Class<TestClass<Integer, String>> rawParentClass = TestClass.class;
    TypeVariable<TestClass<Integer, String>> typeVariable = new TypeVariable<TestClass<Integer, String>>() {}.bind(testClass.getTypeParameters().get(0));

    int index = TypeInfoFactory.getIndex(rawParentClass.getTypeParameters(), typeVariable);
    assertEquals(0, index);
  }

  static class TestClass<A, B> {
    int intField;
    A aField;
    B bField;

    public TypeVariable<TestClass<A, B>> getTypeParameters() {
      return new TypeVariable<TestClass<A, B>>() {};
    }

    public static class NestedClass {
      int nestedIntField;
      String nestedStringField;
    }
  }
}