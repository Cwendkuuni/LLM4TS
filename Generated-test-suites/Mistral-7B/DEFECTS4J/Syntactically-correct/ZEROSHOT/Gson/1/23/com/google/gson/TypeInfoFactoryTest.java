package com.google.gson.test;

import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import com.google.gson.TypeInfoImpl;
import com.google.gson.ParameterizedTypeImpl;
import com.google.gson.GenericArrayTypeImpl;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeLiteral;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class TypeInfoFactoryTest {

 @Test
 public void testTypeInfoForArray() {
 Type type = new int[3].getClass();
 TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(type);
 assertEquals(TypeInfo.ARRAY, typeInfoArray.getType());
 assertEquals(int[].class, typeInfoArray.getTypeComponentType());
 }

 @Test
 public void testTypeInfoForField_TypeVariable() {
 Class<?> rawParentClass = Object.class;
 Field field = mock(Field.class);
 when(field.getType()).thenReturn(new TypeVariable<Integer>() {});
 Type typeDefiningF = new TypeToken<Object>() {}.getType();
 TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
 assertNull(typeInfo.getType());
 assertNotNull(typeInfo.getTypeVariable());
 assertEquals(Integer.TYPE, typeInfo.getTypeComponentType());
 }

 @Test
 public void testTypeInfoForField_Class() {
 Class<?> rawParentClass = Object.class;
 Field field = mock(Field.class);
 when(field.getType()).thenReturn(int[].class);
 Type typeDefiningF = new TypeToken<Object>() {}.getType();
 TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
 assertEquals(TypeInfo.ARRAY, typeInfo.getType());
 assertEquals(int[].class, typeInfo.getTypeComponentType());
 }

 @Test
 public void testTypeInfoForField_ParameterizedType() {
 Class<?> rawParentClass = Object.class;
 Class<?> elementType = Integer.class;
 Field field = mock(Field.class);
 when(field.getType()).thenReturn(new TypeToken<List<Integer>>() {}.getType());
 Type typeDefiningF = new TypeToken<Object>() {}.getType();
 TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
 assertEquals(TypeInfo.COLLECTION, typeInfo.getType());
 assertEquals(new ParameterizedTypeImpl(List.class, new Type[]{elementType}), typeInfo.getTypeComponentType());
 }

 @Test
 public void testTypeInfoForField_ParameterizedType_TypeVariable() {
 Class<?> rawParentClass = Object.class;
 TypeVariable<Integer> intTypeVariable = new TypeVariable<Integer>("T") {};
 Field field = mock(Field.class);
 when(field.getType()).thenReturn(new TypeToken<List<Integer>>() {}.getType());
 Type typeDefiningF = new TypeToken<Object>() {}.getType();
 Type type = new TypeToken<List<Integer>>() {}.getType();
 TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
 assertEquals(TypeInfo.COLLECTION, typeInfo.getType());
 assertEquals(intTypeVariable, typeInfo.getTypeVariable());
 assertEquals(Integer.TYPE, typeInfo.getTypeComponentType());
 }

 @Test
 public void testTypeInfoForField_WildcardType() {
 Class<?> rawParentClass = Object.class;
 Field field = mock(Field.class);
 when(field.getType()).thenReturn(new TypeToken<List<?>>() {}.getType());
 Type typeDefiningF = new TypeToken<Object>() {}.getType();
 TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
 assertEquals(TypeInfo.COLLECTION, typeInfo.getType());
 assertNull(typeInfo.getTypeVariable());
 }

 @Test
 public void testTypeInfoForField_ParameterizedType_Array() {
 Class<?> rawParentClass = Object.class;
 Class<?> elementType = Integer.class;
 Field field = mock(Field.class);
 when(field.getType()).thenReturn(new TypeToken<List<int[]>>() {}.getType());
 Type typeDefiningF = new TypeToken<Object>() {}.getType();
 TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
 assertEquals(TypeInfo.COLLECTION, typeInfo.getType());
 assertEquals(new TypeInfoArray(new TypeInfoImpl(new TypeToken<int[]>() {}.getType())), typeInfo.getTypeComponentType());
 }

 @Test
 public void testTypeInfoForField_ParameterizedType_GenericArray() {
 Class<?> rawParentClass = Object.class;
 Class<?> elementType = Integer.class;
 Field field = mock(Field.class);
 when(field.getType()).thenReturn(new TypeToken<List<Integer[]>>() {}.getType());
 Type typeDefiningF = new TypeToken<Object>() {}.getType();
 TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, typeDefiningF, rawParentClass);
 assertEquals(TypeInfo.COLLECTION, typeInfo.getType());
 assertEquals(new TypeInfoArray(new TypeInfoImpl(new TypeToken<Integer[]>() {}.getType())), typeInfo.getTypeComponentType());
 }
}