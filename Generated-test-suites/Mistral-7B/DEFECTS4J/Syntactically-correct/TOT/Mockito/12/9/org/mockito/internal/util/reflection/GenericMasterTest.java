package org.mockito.internal.util.reflection.test;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() throws NoSuchFieldException {
 // Test case 1: Simple generic field
 Class<GenericMaster> clazz = GenericMaster.class;
 Field field1 = clazz.getDeclaredField("FIELD_NAME_1"); // replace with actual field name
 Class expectedType1 = Integer.class;
 Class actualType1 = new GenericMaster().getGenericType(field1);
 assertEquals(expectedType1, actualType1);

 // Test case 2: Non-generic field
 Class<GenericMaster> clazz = GenericMaster.class;
 Field field2 = clazz.getDeclaredField("FIELD_NAME_2"); // replace with actual field name
 Class expectedType2 = Object.class;
 Class actualType2 = new GenericMaster().getGenericType(field2);
 assertEquals(expectedType2, actualType2);

 // Test case 3: Nested generic types
 Class<GenericMaster> clazz = GenericMaster.class;
 Field field3 = clazz.getDeclaredField("FIELD_NAME_3"); // replace with actual field name
 Class<List<List<Integer>>> expectedType3 = List.class.getTypeParams()[0].getTypeParams()[0];
 Class<List<List<Integer>>> actualType3 = new GenericMaster().getGenericType(field3);
 assertEquals(expectedType3, actualType3);
 }

 // Test case 4: Null field
 @Test(expected = NullPointerException.class)
 public void testNullField() {
 Field nullField = null;
 new GenericMaster().getGenericType(nullField);
 }

 // Test case 5: Field with incorrect getGenericType() return type
 @Test(expected = ClassCastException.class)
 public void testIncorrectReturnType() throws NoSuchFieldException {
 Class<GenericMaster> clazz = GenericMaster.class;
 Field field4 = clazz.getDeclaredField("FIELD_NAME_4"); // replace with actual field name
 Object generic = field4.getGenericType();
 new GenericMaster().getGenericType((Field) generic);
 }

 // Test case 6: Field with no getGenericType()
 @Test(expected = UnsupportedOperationException.class)
 public void testNoGenericType() throws NoSuchFieldException {
 Class<GenericMaster> clazz = GenericMaster.class;
 Field field5 = clazz.getDeclaredField("FIELD_NAME_5"); // replace with actual field name
 new GenericMaster().getGenericType(field5);
 }
}