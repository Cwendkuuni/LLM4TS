package org.junit.junit4;

import org.junit.Test;
import org.mockito.internal.reflection.GenericMaster;
import org.mockito.internal.reflection.FieldTestDouble;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() throws NoSuchFieldException {
 // Test case 1: Testing with a generic field
 Class clazz = TestClassWithGenericField.class;
 GenericMaster genericMaster = new GenericMaster();
 Field field = clazz.getDeclaredField("fieldA");
 field.setAccessible(true);
 Class expected = Integer.class;
 Class actual = genericMaster.getGenericType(field);
 assertEquals(expected, actual);

 // Test case 2: Testing with a non-generic field
 Class nonGenericClazz = TestClassWithNonGenericField.class;
 Field nonGenericField = nonGenericClazz.getDeclaredField("fieldB");
 nonGenericField.setAccessible(true);
 Class expectedNonGeneric = Object.class;
 Class actualNonGeneric = genericMaster.getGenericType(nonGenericField);
 assertEquals(expectedNonGeneric, actualNonGeneric);
 }

 static class TestClassWithGenericField {
 private int fieldA;
 }

 static class TestClassWithNonGenericField {
 private String fieldB;
 }
}