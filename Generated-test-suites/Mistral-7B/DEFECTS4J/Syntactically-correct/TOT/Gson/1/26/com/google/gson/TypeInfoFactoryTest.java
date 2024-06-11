package com.google.gson.tests;

import static com.google.common.truth.Truth.assertThat;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeInfo;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TypeInfoFactoryTest {

  @Test
  public void testTypeInfoForArray() throws NoSuchFieldException {
    Class<Integer[]> arrayClass = Integer[].class;
    Type type = arrayClass.getGenericComponentType();
    TypeInfoArray typeInfo = TypeInfoFactory.TypeInfoForArray(type);
    assertThat(typeInfo.getType()).isEqualTo(arrayClass);
  }

  @Test
  public void testTypeInfoForField() throws NoSuchFieldException {
    Class<Foo> fooClass = Foo.class;
    Field field = fooClass.getDeclaredField("bar");
    Type fieldType = field.getGenericType();
    Type typeDefiningF = fooClass.getGenericSuperclass();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    Class<Integer> intClass = Integer.class;
    assertThat(typeInfo.getType()).isInstanceOf(intClass);
  }

  static class Foo<A> {
    A bar;
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testInvalidTypeInfo() throws NoSuchFieldException {
    Class<Foo> fooClass = Foo.class;
    Field field = fooClass.getDeclaredField("qux");
    Type fieldType = field.getGenericType();
    Type typeDefiningF = fooClass.getGenericSuperclass();
    TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
  }

  @Test
  public void testTypeInfoForWildcard() {
    Type listType = new TypeToken<List<?>>() {}.getType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(null, listType);
    assertThat(typeInfo.getType()).isNull();
  }

  @Test
  public void testTypeInfoForParameterized() {
    Type listType = new TypeToken<List<Integer>>() {}.getType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(null, listType);
    assertThat(typeInfo.getType()).isInstanceOf(List.class);
    assertThat(typeInfo.getType()).asInstanceOf(List.class).getType().equals(Integer.class);
  }

  @Test
  public void testTypeInfoForArrayInParameterized() {
    Type listType = new TypeToken<List<Integer[]>>() {}.getType();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(null, listType);
    assertThat(typeInfo.getType()).isInstanceOf(List.class);
    assertThat(typeInfo.getType()).asInstanceOf(List.class).getType().isArray();
    assertThat(typeInfo.getType().getComponentType()).isInstanceOf(Integer[].class);
  }

  @Test
  public void testTypeInfoForTypeVariable() throws NoSuchFieldException {
    Class<Foo<Bar>> fooClass = Foo.class.getTypeName().replace("<A>", "<Bar>").asSubclass(Foo.class);
    Field field = fooClass.getDeclaredField("bar");
    Type fieldType = field.getGenericType();
    Type typeDefiningF = fooClass.getGenericSuperclass();
    TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
    Class<Bar> barClass = Bar.class;
    assertThat(typeInfo.getType()).isInstanceOf(barClass);
  }

  static class Foo<A> {
    A bar;
  }

  static class Bar {}
}