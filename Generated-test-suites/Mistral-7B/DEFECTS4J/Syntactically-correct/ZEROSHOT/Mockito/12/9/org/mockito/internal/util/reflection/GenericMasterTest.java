package org.junit.test; // replace this with your actual test package

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() throws NoSuchFieldException {
 // create an instance of the class under test
 GenericMaster genericMaster = new GenericMaster();

 // create a field with a generic type
 Class clazz = TestClassWithGenericField.class;
 Field field = clazz.getDeclaredField("genericField");
 field.setAccessible(true);

 // test with a generic field
 Class expectedType = Integer.class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);

 // test with a non-generic field
 field = clazz.getDeclaredField("nonGenericField");
 field.setAccessible(true);
 actualType = genericMaster.getGenericType(field);
 assertEquals(Object.class, actualType);
 }

 @Test
 public void testGetGenericTypeWithNestedGenerics() throws NoSuchFieldException {
 // create an instance of the class under test
 GenericMaster genericMaster = new GenericMaster();

 // create a field with nested generic types
 Class clazz = TestClassWithNestedGenerics.class;
 Field field = clazz.getDeclaredField("nestedGenericField");
 field.setAccessible(true);

 // test with a nested generic field
 Class expectedType = String[].class;
 Class actualType = genericMaster.getGenericType(field);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetGenericTypeWithNullField() {
 // create an instance of the class under test
 GenericMaster genericMaster = new GenericMaster();

 // test with null field
 Field field = null;
 try {
 genericMaster.getGenericType(field);
 } catch (NullPointerException e) {
 fail("Expected IllegalArgumentException, but got NullPointerException");
 } catch (IllegalArgumentException e) {
 // expected exception
 }
 }
}

class TestClassWithGenericField {
 private Integer genericField;

 public Integer getGenericField() {
 return genericField;
 }

 public void setGenericField(Integer genericField) {
 this.genericField = genericField;
 }
}

class TestClassWithNestedGenerics {
 private List<String> nestedGenericField;

 public List<String> getNestedGenericField() {
 return nestedGenericField;
 }

 public void setNestedGenericField(List<String> nestedGenericField) {
 this.nestedGenericField = nestedGenericField;
 }
}