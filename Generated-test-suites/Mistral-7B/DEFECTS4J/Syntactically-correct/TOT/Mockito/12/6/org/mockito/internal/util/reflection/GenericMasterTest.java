package org.mockito.internal.util.reflection.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.TestFields;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

 private GenericMaster genericMaster;
 private TestFields testFields;

 @Before
 public void setUp() {
 genericMaster = new GenericMaster();
 testFields = new TestFields();
 }

 @Test
 public void testGetGenericType_SimpleField() {
 Field simpleField = testFields.simpleField;
 Class expectedType = Integer.TYPE;
 Class actualType = genericMaster.getGenericType(simpleField);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_NullField() {
 Field nullField = null;
 Class expectedType = Object.class;
 Class actualType = genericMaster.getGenericType(nullField);
 assertEquals(expectedType, actualType);
 }

 @Test
 public void testGetGenericType_NonGenericField() {
 Field nonGenericField = testFields.nonGenericField;
 Class expectedType = Object.class;
 Class actualType = genericMaster.getGenericType(nonGenericField);
 assertEquals(expectedType, actualType);
 }

 @Test(expected = NullPointerException.class)
 public void testGetGenericType_NullGenericType() {
 Field fieldWithNullGenericType = testFields.fieldWithNullGenericType;
 genericMaster.getGenericType(fieldWithNullGenericType);
 }

 @Test
 public void testGetGenericType_NestedGenericType() {
 Field nestedField = testFields.nestedField;
 Class expectedType = String[].class;
 Class actualType = genericMaster.getGenericType(nestedField);
 assertEquals(expectedType, actualType);
 }
}