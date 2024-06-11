package org.junit.junit4;

import org.junit.Test;
import org.mockito.internal.reflection.GenericMaster;
import org.mockito.internal.reflection.FieldTestDouble;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GenericMasterTest {

 private GenericMaster genericMaster = new GenericMaster();
 private Field field;

 @Test
 public void testGetGenericTypeWithGenericField() throws NoSuchFieldException {
     Class clazz = SomeClassWithGenericField.class;
     field = clazz.getDeclaredField("genericField");

     Class expected = String.class;
     Class actual = genericMaster.getGenericType(field);

     assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericTypeWithNonGenericField() throws NoSuchFieldException {
     Class clazz = SomeClassWithNonGenericField.class;
     field = clazz.getDeclaredField("nonGenericField");

     Class expected = Object.class;
     Class actual = genericMaster.getGenericType(field);

     assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericTypeWithNullField() {
     assertNull(genericMaster.getGenericType(null));
 }

 @Test
 public void testGetGenericTypeWithNullClass() {
     assertNull(genericMaster.getGenericType(null));
 }

 @Test
 public void testGetGenericTypeWithInvalidField() {
     Class clazz = SomeInvalidClass.class;
     assertThrows(NoSuchFieldException.class, () -> {
         field = clazz.getDeclaredField("nonExistentField");
         genericMaster.getGenericType(field);
     });
 }

 @Test
 public void testGetGenericTypeWithFieldWithoutType() throws NoSuchFieldException {
     Class clazz = SomeClassWithoutTypeField.class;
     field = clazz.getDeclaredField("nonTypeField");

     Class expected = Object.class;
     Class actual = genericMaster.getGenericType(field);

     assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericTypeWithNestedGenericTypes() throws NoSuchFieldException {
     Class clazz = SomeClassWithNestedGenericTypes.class;
     field = clazz.getDeclaredField("nestedGenericField");

     Class<List<Integer>> expected = List.class.getTypeName().replace("List<", "").replace(">", "").replace(Character.toString('T'), "Integer").asSubtype(List.class);
     Class actual = genericMaster.getGenericType(field);

     assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericTypeWithArrayField() throws NoSuchFieldException {
     Class clazz = SomeClassWithArrayField.class;
     field = clazz.getDeclaredField("arrayField");

     Class expected = int[].class;
     Class actual = genericMaster.getGenericType(field);

     assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericTypeWithParameterizedType() throws NoSuchFieldException {
     Class clazz = SomeClassWithParameterizedTypeField.class;
     field = clazz.getDeclaredField("parameterizedTypeField");

     Class<Map<String, Integer>> expected = Map.class.getTypeName().replaceAll("Map<(.*), (.*>)>", "Map<$1, $2>").replace("Map<", "").replace(">", "").asSubtype(Map.class);
     Class actual = genericMaster.getGenericType(field);

     assertEquals(expected, actual);
 }

 @Test
 public void testGetGenericTypeWithWildcardType() throws NoSuchFieldException {
     Class clazz = SomeClassWithWildcardTypeField.class;
     field = clazz.getDeclaredField("wildcardTypeField");

     Class expected = List.class.getTypeName().replaceAll("List<(.*)>", "List<?>").asSubtype(List.class);
     Class actual = genericMaster.getGenericType(field);

     assertEquals(expected, actual);
 }

 static class SomeClassWithGenericField {
     @FieldTestDouble
     private List<String> genericField;
 }

 static class SomeClassWithNonGenericField {
     @FieldTestDouble
     private int nonGenericField;
 }

 static class SomeInvalidClass {
 }

 static class SomeClassWithoutTypeField {
     @FieldTestDouble
     private Object nonTypeField;
 }

 static class SomeClassWithNestedGenericTypes {
     @FieldTestDouble
     private List<List<Integer>> nestedGenericField;
 }

 static class SomeClassWithArrayField {
     @FieldTestDouble
     private int[] arrayField;
 }

 static class SomeClassWithParameterizedTypeField<T> {
     @FieldTestDouble
     private Map<T, T> parameterizedTypeField;
 }

 static class SomeClassWithWildcardTypeField<T> {
     @FieldTestDouble
     private List<T> wildcardTypeField;
 }
}