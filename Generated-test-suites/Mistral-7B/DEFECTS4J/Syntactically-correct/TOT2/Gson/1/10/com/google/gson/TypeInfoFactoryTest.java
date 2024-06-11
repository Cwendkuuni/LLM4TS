// Your tests go here
package com.example.junit;

import static org.junit.Assert.*;

import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.junit.Test;

public class TypeInfoFactoryTest {

@Test
public void testTypeInfoForArray() {
Type type = new int[5].getClass(); // Primitive array
TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
assertEquals(TypeInfoArray.class, typeInfoArray.getClass());
assertNotNull(typeInfoArray.getType());
assertEquals(int[].class, typeInfoArray.getType());

Type typeObject = new Object[5].getClass(); // Object array
TypeInfoArray typeInfoObject = TypeInfoFactory.getTypeInfoForArray(typeObject);
assertEquals(TypeInfoArray.class, typeInfoObject.getClass());
assertNotNull(typeInfoObject.getType());
assertEquals(Object[].class, typeInfoObject.getType());
}

@Test
public void testTypeInfoForField() {
try {
Class<ExampleClass> exampleClass = ExampleClass.class;
Field field = exampleClass.getDeclaredField("exampleField");
Type typeDefiningF = field.getDeclaringClass();
Type type = field.getType();
TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
assertEquals(TypeInfo.class, typeInfo.getClass());
assertNotNull(typeInfo.getType());
assertEquals(int.class, typeInfo.getType());
} catch (NoSuchFieldException e) {
throw new RuntimeException(e);
}
}

@Test(expected = NullPointerException.class)
public void testTypeInfoForField_NullInput() {
Type typeDefiningF = null;
Field field = ExampleClass.class.getDeclaredField("exampleField");
Type type = field.getType();
TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
}

@Test(expected = IllegalArgumentException.class)
public void testTypeInfoForField_NonFieldType() {
Class<ExampleClass> exampleClass = ExampleClass.class;
Method method = exampleClass.getDeclaredMethod("exampleMethod");
Field field = exampleClass.getDeclaredField("exampleField");
Type typeDefiningF = method;
Type type = field.getType();
TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
}

@Test(expected = IllegalArgumentException.class)
public void testTypeInfoForField_NoSuchField() {
Type typeDefiningF = ExampleClass.class;
Field field = null;
Type type = null;
TypeInfoFactory.getTypeInfoForField(field, typeDefiningF);
}

static class ExampleClass {
int exampleField;

void exampleMethod() {}
}
}
// Your tests go here