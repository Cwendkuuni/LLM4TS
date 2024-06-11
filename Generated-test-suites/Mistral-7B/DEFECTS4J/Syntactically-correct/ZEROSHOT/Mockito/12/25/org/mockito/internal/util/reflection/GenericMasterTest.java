package org.junit. TEST_PACKAGE; // replace TEST_PACKAGE with the actual package name of your test class

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class GenericMasterTest {

 private GenericMaster genericMaster;

 @Before
 public void setup() {
 genericMaster = new GenericMaster();
 }

 @Test
 public void testGetGenericType_whenFieldIsGeneric_shouldReturnGenericType() throws NoSuchFieldException {
 Class<?> clazz = TestClassWithGenericField.class;
 Field field = clazz.getDeclaredField("fieldName"); // replace "fieldName" with the actual name of the generic field
 field.setAccessible(true);

 Class<?> expectedGenericType = String.class; // replace with the expected generic type
 Class<?> actualGenericType = genericMaster.getGenericType(field);

 assertEquals("getGenericType should return the expected generic type", expectedGenericType, actualGenericType);
 }

 @Test
 public void testGetGenericType_whenFieldIsNotGeneric_shouldReturnObjectClass() throws NoSuchFieldException {
 Class<?> clazz = TestClassWithoutGenericField.class;
 Field field = clazz.getDeclaredField("fieldName"); // replace "fieldName" with the actual name of the non-generic field
 field.setAccessible(true);

 assertNotNull("getGenericType should not return null when field is not generic", genericMaster);
 Class<?> actualGenericType = genericMaster.getGenericType(field);

 assertEquals("getGenericType should return Object.class when field is not generic", Object.class, actualGenericType);
 }

 @Test(expected = NoSuchFieldException.class)
 public void testGetGenericType_whenFieldDoesNotExist_shouldThrowException() throws Exception {
 Class<?> clazz = TestClassWithGenericField.class;
 String nonExistentFieldName = "nonExistentFieldName"; // replace with a non-existent field name

 assertThrows("getGenericType should throw NoSuchFieldException when field does not exist", NoSuchFieldException.class, () -> {
 Field field = clazz.getDeclaredField(nonExistentFieldName);
 genericMaster.getGenericType(field);
 });
 }

 @Test(expected = MockitoException.class)
 public void testGetGenericType_whenFieldIsFinal_shouldThrowMockitoException() throws NoSuchFieldException {
 Class<?> clazz = TestClassWithFinalGenericField.class;
 Field field = clazz.getDeclaredField("fieldName"); // replace "fieldName" with the actual name of the final generic field
 field.setAccessible(true);
 field.setFinal(false); // set final to false to test

 assertThrows("getGenericType should throw MockitoException when trying to access final field", MockitoException.class, () -> {
 genericMaster.getGenericType(field);
 field.setAccessible(true); // set accessible again to reset state
 field.setFinal(true); // reset final state
 });
 }
}