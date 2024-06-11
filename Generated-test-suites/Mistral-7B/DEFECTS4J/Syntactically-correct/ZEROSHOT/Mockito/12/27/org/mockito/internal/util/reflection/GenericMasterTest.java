package org.junit.tests.mockito.internal.util.reflection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.internal.util.reflection.GenericMaster.getGenericType;

@RunWith(MockitoJUnitRunner.class)
public class GenericMasterTest {

 private static final Class<Integer> INT_TYPE = Integer.TYPE;
 private static final Class<List<String>> STRINGS_LIST_TYPE = new ArrayList<String>().getClass();

 @Test
 public void testGetGenericTypeNullField() {
 Field nullField = null;
 Class result = getGenericType(nullField);
 assertEquals(Object.class, result);
 }

 @Test
 public void testGetGenericTypeFieldWithObjectType() {
 Class clazz = SomeClassWithObjectField.class;
 try {
 Field objectField = clazz.getDeclaredField("objectField");
 objectField.setAccessible(true);
 Class result = getGenericType(objectField);
 assertEquals(Object.class, result);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetGenericTypeFieldWithGenericType() {
 Class clazz = SomeClassWithGenericField.class;
 try {
 Field genericField = clazz.getDeclaredField("genericField");
 genericField.setAccessible(true);
 Class<List<Integer>> expectedResult = new ArrayList<Integer>().getType();
 Class result = getGenericType(genericField);
 assertEquals(expectedResult, result);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetGenericTypeFieldWithNestedGenericType() {
 Class clazz = SomeClassWithNestedGenericField.class;
 try {
 Field nestedGenericField = clazz.getDeclaredField("nestedGenericField");
 nestedGenericField.setAccessible(true);
 Class<List<List<String>>> expectedResult = new ArrayList<List<String>>().getType();
 Class result = getGenericType(nestedGenericField);
 assertEquals(expectedResult, result);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetGenericTypeFieldWithArrayType() {
 Class clazz = SomeClassWithArrayField.class;
 try {
 Field arrayField = clazz.getDeclaredField("arrayField");
 arrayField.setAccessible(true);
 Class<Integer[]> expectedResult = Integer[].class;
 Class result = getGenericType(arrayField);
 assertEquals(expectedResult, result);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetGenericTypeFieldWithParameterizedArrayType() {
 Class clazz = SomeClassWithParameterizedArrayField.class;
 try {
 Field parameterizedArrayField = clazz.getDeclaredField("parameterizedArrayField");
 parameterizedArrayField.setAccessible(true);
 Class<List<Integer[]>> expectedResult = new ArrayList<Integer[]>().getType();
 Class result = getGenericType(parameterizedArrayField);
 assertEquals(expectedResult, result);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetGenericTypeFieldWithUnknownType() {
 Class clazz = SomeClassWithUnknownField.class;
 try {
 Field unknownField = clazz.getDeclaredField("unknownField");
 unknownField.setAccessible(true);
 Class result = getGenericType(unknownField);
 assertEquals(Object.class, result);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetGenericTypeFieldWithPrimitiveType() {
 Class clazz = SomeClassWithPrimitiveField.class;
 try {
 Field primitiveField = clazz.getDeclaredField("primitiveField");
 primitiveField.setAccessible(true);
 Class<Integer> expectedResult = INT_TYPE;
 Class result = getGenericType(primitiveField);
 assertEquals(expectedResult, result);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testGetGenericTypeFieldWithPrimitiveWrapperType() {
 Class clazz = SomeClassWithPrimitiveWrapperField.class;
 try {
 Field primitiveWrapperField = clazz.getDeclaredField("primitiveWrapperField");
 primitiveWrapperField.setAccessible(true);
 Class<Integer> expectedResult = INT_TYPE;
 Class result = getGenericType(primitiveWrapperField);
 assertEquals(expectedResult, result);
 } catch (NoSuchFieldException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 private static class SomeClassWithObjectField {
 private Object objectField;
 }

 private static class SomeClassWithGenericField<T> {
 private T genericField;
 }

 private static class SomeClassWithNestedGenericField<S, T> {
 private List<S> nestedGenericField;
 }

 private static class SomeClassWithArrayField {
 private Integer[] arrayField;
 }

 private static class SomeClassWithParameterizedArrayField<T> {
 private List<T>[] parameterizedArrayField;
 }

 private static class SomeClassWithUnknownField {
 private Object unknownField;
 }

 private static class SomeClassWithPrimitiveField {
 private int primitiveField;
 }

 private static class SomeClassWithPrimitiveWrapperField {
 private Integer primitiveWrapperField;
 }
}