package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.reflectiontestutils.FieldTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GenericMasterTest {

 @Test
 public void testGetGenericTypeForSimpleField() {
 Field field = FieldTestUtils.getField(GenericMaster.class, "genericTypeField");
 Class expectedType = Integer.class;
 Class actualType = new GenericMaster().getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericTypeForNullField() {
 Class actualType = new GenericMaster().getGenericType(null);
 assertEquals(Object.class, actualType);
 }

 @Test
 public void testGetGenericTypeForNonGenericField() {
 Field field = FieldTestUtils.getField(GenericMaster.class, "nonGenericField");
 Class actualType = new GenericMaster().getGenericType(field);
 assertEquals(Object.class, actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericTypeForNullGenericType() {
 Field field = FieldTestUtils.getField(GenericMaster.class, "genericTypeField");
 Type genericType = null;
 field.setAccessible(true);
 try {
 genericType = field.getGenericType();
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 Class actualType = new GenericMaster().getGenericType((Type) genericType);
 }

 @Test
 public void testGetGenericTypeForNestedGenerics() {
 Class<Map<List<String>, Integer>> expectedType = Map.class; // java.util.Map
 Class<List<String>> listType = List.class; // java.util.List
 Class<String> stringType = String.class; // java.lang.String
 Class<Integer> integerType = Integer.class; // java.lang.Integer

 Field field = FieldTestUtils.getField(GenericMaster.class, "nestedGenericsField");
 Type genericType = field.getGenericType();
 ParameterizedType parameterizedType = (ParameterizedType) genericType;
 Type[] actualTypes = parameterizedType.getActualTypeArguments();
 Class<?> firstActualType = (Class<?>) actualTypes[0];
 Class<?> secondActualType = (Class<?>) actualTypes[1];
 assertEquals(expectedType, firstActualType.getComponentType());
 assertEquals(listType, secondActualType.getComponentType());
 }
}