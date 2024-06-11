package org.junit.test; // replace with your test package if necessary

import org.junit.Test;
import org.mockito.internal.reflection.GenericMaster;
import org.mockito.internal.reflection.FieldTestDouble;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.reflection.ReflectionUtils.getDeclaredField;

public class GenericMasterTest {

 private GenericMaster genericMaster = new GenericMaster();

 @Test
 public void testGetGenericType() throws IllegalAccessException {
     GenericMaster obj = new GenericMaster();
     Field field = FieldTestDouble.class.getDeclaredField("field");
     field.setAccessible(true);

     // Test for non-generic field
     assertEquals(Object.class, genericMaster.getGenericType(nullField));

     // Test for generic field with single type argument
     assertEquals(String.class, genericMaster.getGenericType(field));

     // Test for generic field with multiple type arguments
     Field fieldWithTwoArguments = FieldWithTwoArguments.class.getDeclaredField("field");
     fieldWithTwoArguments.setAccessible(true);
     assertEquals(List.class, genericMaster.getGenericType(fieldWithTwoArguments));
     assertEquals(Integer.class, genericMaster.getGenericType(((ParameterizedType) fieldWithTwoArguments.getGenericType()).getActualTypeArguments()[1]));

     // Test for nested generic type
     Field fieldWithNestedGenericType = FieldWithNestedGenericType.class.getDeclaredField("field");
     fieldWithNestedGenericType.setAccessible(true);
     assertEquals(List.class, genericMaster.getGenericType(fieldWithNestedGenericType));
     assertEquals(Map.class, genericMaster.getGenericType(((ParameterizedType) fieldWithNestedGenericType.getGenericType()).getActualTypeArguments()[1]));
     assertEquals(Integer.class, genericMaster.getGenericType(((ParameterizedType) ((ParameterizedType) ((ParameterizedType) ((ParameterizedType) ((ParameterizedType) fieldWithNestedGenericType.getGenericType()).getActualTypeArguments()[1])).getActualTypeArguments()[0]))));
 }

 private static Field nullField;
 private static class FieldTestDouble {
     public Field field;

     static {
         try {
             nullField = FieldTestDouble.class.getDeclaredField("field");
             nullField.setAccessible(true);
         } catch (NoSuchFieldException e) {
             throw new RuntimeException(e);
         }
     }

     private Field field;
 }

 private static class FieldWithTwoArguments<T, U> {
     public T field;
 }

 private static class FieldWithNestedGenericType<T, U> {
     public Map<Integer, T> field;
 }
}