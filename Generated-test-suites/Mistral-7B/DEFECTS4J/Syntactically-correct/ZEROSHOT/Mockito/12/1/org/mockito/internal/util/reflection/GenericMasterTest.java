package org.junit.test;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() throws NoSuchFieldException {
 final Class clazz = FieldTestDouble.class;
 final Field field = clazz.getDeclaredField("testField");
 field.setAccessible(true);

 // Test when field is not generic
 {
 GenericMaster genericMaster = new GenericMaster();
 assertEquals(Object.class, genericMaster.getGenericType(nullField));
 }

 // Test when field is generic and returns the correct type
 {
 GenericMaster genericMaster = new GenericMaster();
 assertEquals(Integer.class, genericMaster.getGenericType(field));
 }
 }

 private Field nullField = null;
 private static class FieldTestDouble {
 private int testField;
 }

}