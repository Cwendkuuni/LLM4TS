package com.google.gson.tests;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import org.junit.Test;

public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() {
    Type type = new int[10].getClass().getComponentType();
    TypeInfo expected = new TypeInfoArray(type);
    TypeInfo actual = TypeInfoFactory.getTypeInfoForArray(type);
    assertEquals(expected, actual);
  }

  @Test
  public void testTypeInfoForField() throws Exception {
    Class<?> clazz = Foo.class;
    Field field = clazz.getDeclaredField("intField");
    Type type = field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    TypeInfo expected = new TypeInfo(Integer.TYPE);
    TypeInfo actual = TypeInfoFactory.getTypeInfoForField(field, parentType);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForInvalidType() {
    Class<?> clazz = Foo.class;
    Class<?> rawParentClass = Object.class;
    Field field = clazz.getDeclaredField("invalidField");
    Type type = field.getGenericType();
    Type parentType = clazz.getGenericSuperclass();
    TypeInfoFactory.getTypeInfoForField(field, parentType, rawParentClass);
  }

  @Test
  public void testGetActualTypeForClass() {
    Class<?> rawParentClass = Foo.class;
    Type parentType = rawParentClass.getGenericSuperclass();
    Type expected = Integer.TYPE;
    Type actual = TypeInfoFactory.getActualType(Integer.TYPE, parentType, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualTypeForParameterizedType() {
    Class<?> rawParentClass = Foo.class;
    Type parentType = rawParentClass.getGenericSuperclass();
    Type expected = new ParameterizedTypeImpl(List.class, new Type[] { Integer.TYPE });
    Type actual = TypeInfoFactory.getActualType(
        new ParameterizedTypeImpl(List.class, new Type[] { new TypeVariable<>("T") }), parentType, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualTypeForGenericArrayType() {
    Class<?> rawParentClass = Foo.class;
    Type parentType = rawParentClass.getGenericSuperclass();
    Type componentType = Integer.TYPE;
    Type expected = new GenericArrayTypeImpl(componentType);
    Type actual = TypeInfoFactory.getActualType(new GenericArrayTypeImpl(componentType), parentType, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetActualTypeForTypeVariable() {
    Class<?> rawParentClass = Foo.class;
    Type parentType = rawParentClass.getGenericSuperclass();
    TypeVariable<?> fieldTypeVariable = new TypeVariable<>("T");
    int index = 0;
    Type expected = Integer.TYPE;
    Type actual = TypeInfoFactory.getTypeInfoForField(
        new Foo().getClass().getDeclaredField("intField"), parentType, rawParentClass).getType();
    checkArgument(index >= 0 && index < parentType.getActualTypeArguments().length, "Invalid index");
    Type[] actualTypeArguments = (Type[]) parentType.getActualTypeArguments();
    Type actualTypeArgument = actualTypeArguments[index];
    assertEquals(expected.getTypeName(), actualTypeArgument.getTypeName());
    assertInstanceOf(Integer.TYPE.getClass(), actualTypeArgument);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testGetActualTypeForWildcardType() {
    Class<?> rawParentClass = Foo.class;
    Type parentType = rawParentClass.getGenericSuperclass();
    Type wildcardType = new WildcardType(null, null);
    Type expected = Object.class;
    Type actual = TypeInfoFactory.getActualType(wildcardType, parentType, rawParentClass);
    assertEquals(expected, actual);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testExtractRealTypes() {
    Type[] actualTypeArguments = new Type[0];
    Type parentType = new ParameterizedTypeImpl(List.class, new Type[] { new TypeVariable<>("T") });
    Type[] expected = {Integer.TYPE};
    TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, new Object() {}.getClass());
  }

  @Test
  public void testExtractRealTypesValid() {
    Type[] actualTypeArguments = new ParameterizedTypeImpl(List.class, new Type[] { Integer.TYPE, Long.TYPE })
        .getActualTypeArguments();
    Type[] expected = {Integer.TYPE, Long.TYPE};
    assertArrayEquals(expected, actualTypeArguments);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetIndexForNonExistentTypeVariable() {
    TypeVariable<?>[] types = new TypeVariable[0].getClass().getTypeParameters();
    TypeVariable<?> type = new TypeVariable<>("Invalid");
    TypeInfoFactory.getIndex(types, type);
  }

  @Test
  public void testGetIndexForExistentTypeVariable() {
    TypeVariable<?>[] types = new TypeVariable[1].getClass().getTypeParameters();
    TypeVariable<?> type = types[0];
    int index = TypeInfoFactory.getIndex(types, type);
    assertEquals(0, index);
  }

  static class Foo {
    int intField;
  }

  static class Bar<T> extends Foo {
    T listField;
  }

  static class List<T> {}
}