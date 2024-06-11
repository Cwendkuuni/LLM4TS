package org.junit.junit4;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

 private GenericMaster genericMaster = new GenericMaster();

 @Test
 public void testGetGenericType() {
     Class clazz = FieldTestDouble.class;
     Field field = null;
     try {
         field = clazz.getDeclaredField("testField");
     } catch (NoSuchFieldException e) {
         e.printStackTrace();
     }
     field.setAccessible(true);

     Class expectedType = String.class;
     Class actualType = genericMaster.getGenericType(field);

     assertEquals(expectedType, actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericTypeWhenFieldIsNull() {
     Class actualType = genericMaster.getGenericType(null);
 }

 @Test
 public void testGetGenericTypeWhenFieldIsNotGeneric() {
     Class clazz = Integer.class;
     Field field = null;
     try {
         field = clazz.getDeclaredField("value");
     } catch (NoSuchFieldException e) {
         e.printStackTrace();
     }
     field.setAccessible(true);

     Class actualType = genericMaster.getGenericType(field);

     assertEquals(Object.class, actualType);
 }

 @Test
 public void testGetGenericTypeWhenFieldHasMultipleTypes() {
     Class<Map<List<String>, Integer>> clazz = Map.class;
     Field field = null;
     try {
         field = clazz.getDeclaredField("entrySet");
     } catch (NoSuchFieldException e) {
         e.printStackTrace();
     }
     field.setAccessible(true);

     Type generic = field.getGenericType();
     ParameterizedType parameterizedType = (ParameterizedType) generic;
     Type[] actualTypes = parameterizedType.getActualTypeArguments();

     Class<List<String>> expectedFirstType = Arrays.asList(String.class).getType();
     Class<Integer> expectedSecondType = Integer.class;

     Class<?> actualFirstType = genericMaster.getGenericType((Field) Arrays.asList(actualTypes[0]).toArray()[0]);
     Class<?> actualSecondType = genericMaster.getGenericType((Field) Arrays.asList(actualTypes[1]).toArray()[0]);

     assertEquals(expectedFirstType, actualFirstType);
     assertEquals(expectedSecondType, actualSecondType);
 }
}