package com.example.junit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfo;
import com.google.gson.TypeUtils;
import org.junit.Test;

public class TypeInfoFactoryTest {
  private static class TestClass<T> {
    private T field;
  }

  @Test
  public void testTypeInfoForArray() {
    Type type = new Type[] { int[].class }.getClass();
    TypeInfoArray typeInfo = TypeInfoFactory.TypeInfoForArray(type);
    assertNotNull(typeInfo);
    assertEquals(TypeInfo.ARRAY, typeInfo.type);
    assertEquals(int[].class, typeInfo.componentType);
  }

  @Test
  public void testTypeInfoForField() {
    TestClass<String> testClass = new TestClass<>();
    Field field = TestClass.class.getField("field");
    Type type = field.getType();
    TypeInfo typeInfo = TypeInfoFactory.TypeInfoForField(field, type.getClass());
    assertNotNull(typeInfo);
    assertEquals(TypeInfo.FIELD, typeInfo.type);
    assertEquals(String.class, typeInfo.type);
  }

  @Test
  public void testGetActualType() {
    // Create mocks
    Field fieldMock = mock(Field.class);
    Type typeDefiningFMock = mock(Type.class);
    Class<?> rawParentClassMock = mock(Class.class);

    // Set up mocks
    when(fieldMock.getGenericType()).thenReturn(new Type[] { String.class }.getClass());
    when(typeDefiningFMock.getClass()).thenReturn(TestClass.class);
    when(TypeUtils.toRawClass(typeDefiningFMock)).thenReturn(TestClass.class);

    // Call method under test
    Type actualType = TypeInfoFactory.getActualType(
        new Type[] { String.class }.getClass(), typeDefiningFMock, rawParentClassMock);

    // Verify mocks
    verify(fieldMock).getGenericType();
    verify(typeDefiningFMock).getClass();
    verifyZeroInteractions(rawParentClassMock);

    // Assert results
    assertEquals(String.class, actualType);
  }

  // Edge cases and exception handling scenarios

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForArray_NullType() {
    Type type = null;
    TypeInfoArray typeInfo = TypeInfoFactory.TypeInfoForArray(type);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForArray_NonArrayType() {
    Type type = String.class;
    TypeInfoArray typeInfo = TypeInfoFactory.TypeInfoForArray(type);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForField_NullField() {
    Type type = String.class.getTypeName();
    TypeInfoFactory.TypeInfoForField(null, type);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForField_NullTypeDefiningF() {
    Field field = new Field("field", Integer.TYPE);
    TypeInfoFactory.TypeInfoForField(field, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForField_NonFieldType() {
    Type type = String.class;
    TypeInfoFactory.TypeInfoForField(null, type);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testTypeInfoForField_UnmatchedTypeVariable() {
    TestClass<List<String>> testClass = new TestClass<>();
    Field field = TestClass.class.getField("field");
    Type type = field.getType();
    TypeInfoFactory.TypeInfoForField(field, type);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualType_NullTypeToEvaluate() {
    Type typeDefiningF = new Type[] { String.class }.getClass();
    Class<?> rawParentClass = TestClass.class;
    TypeInfoFactory.getActualType(null, typeDefiningF, rawParentClass);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualType_NullParentType() {
    Type typeToEvaluate = new Type[] { String.class }.getClass();
    Class<?> rawParentClass = TestClass.class;
    TypeInfoFactory.getActualType(typeToEvaluate, null, rawParentClass);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetActualType_NullRawParentClass() {
    Type typeToEvaluate = new Type[] { String.class }.getClass();
    Type parentType = typeToEvaluate.getClass();
    TypeInfoFactory.getActualType(typeToEvaluate, parentType, null);
  }
}