
package com.example.junit4test;

import static org.junit.Assert.*;
import com.google.gson.TypeInfoFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class TypeInfoFactoryTest {

  static class TestClass {
    int a;
  }

  @Test
  public void testTypeInfoForArray() {
    Type integerType = new TypeToken<Integer[]>() {}.getType();
    TypeInfoArray intArrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(integerType);
    assertEquals(TypeInfo.ARRAY, intArrayTypeInfo.type);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForArrayNullType() {
    TypeInfoFactory.getTypeInfoForArray(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForArrayEmptyArray() {
    Type emptyArrayType = new TypeToken<Integer[]>() {}.getType();
    emptyArrayType.getComponentType(); // Remove the component type to create an empty array type
    TypeInfoFactory.getTypeInfoForArray(emptyArrayType);
  }

  @Test(expected = NullPointerException.class)
  public void testTypeInfoForFieldNullField() throws NoSuchFieldException, IllegalAccessException {
    Field field = null;
    Type classType = TestClass.class.getType();
    TypeInfoFactory.getTypeInfoForField(field, classType);
  }

  @Test(expected = NullPointerException.class)
  public void testTypeInfoForFieldNullTypeDefiningF() throws NoSuchFieldException, IllegalAccessException {
    Field field = TestClass.class.getField("a");
    Type nullType = null;
    TypeInfoFactory.getTypeInfoForField(field, nullType);
  }

  @Test(expected = NoSuchFieldException.class)
  public void testTypeInfoForFieldNonExistentField() throws NoSuchFieldException {
    Field nonExistentField = null;
    Type classType = TestClass.class.getType();
    TypeInfoFactory.getTypeInfoForField(nonExistentField, classType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForFieldInvalidFieldType() throws NoSuchFieldException, IllegalAccessException {
    Field field = TestClass.class.getField("a");
    Type classType = TestClass.class.getType();
    TypeInfoFactory.getTypeInfoForField(field, classType);
  }

  @Test
  public void testTypeInfoForFieldValidType() throws NoSuchFieldException, IllegalAccessException {
    Field field = TestClass.class.getField("a");
    Type classType = TestClass.class.getType();
    TypeInfo intTypeInfo = TypeInfoFactory.getTypeInfoForField(field, classType);
    assertEquals(int.class, intTypeInfo.type);
  }

  @Test
  public void testTypeInfoForFieldValidType2() throws NoSuchFieldException, IllegalAccessException {
    Field field = TestClass.class.getField("a");
    Type classType = TestClass.class.getType();
    TypeInfo intTypeInfo = TypeInfoFactory.getTypeInfoForField(field, new TypeToken<TestClass>() {}.getType());
    assertEquals(int.class, intTypeInfo.type);
  }
}
