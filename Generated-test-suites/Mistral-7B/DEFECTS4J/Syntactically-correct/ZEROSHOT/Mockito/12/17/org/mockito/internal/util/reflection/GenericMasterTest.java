package org.junit.test; // replace with your test package name if necessary

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() throws NoSuchFieldException {
 Field field = new FieldTestDouble().getClass().getDeclaredField("fieldToTest"); // replace with your field name
 field.setAccessible(true);

 Class expectedType = String.class; // replace with your expected type
 Class actualType = new GenericMaster().getGenericType(field);

 assertEquals(expectedType, actualType);
 }

 @Test (expected = NullPointerException.class)
 public void testGetGenericTypeWhenFieldHasNoGenericType() throws NoSuchFieldException {
 Field field = new FieldTestDouble().getClass().getDeclaredField("nonGenericField"); // replace with your non-generic field name
 field.setAccessible(true);

 new GenericMaster().getGenericType(field);
 }

 @Test
 public void testGetGenericTypeWhenFieldHasNoActualTypeArguments() throws NoSuchFieldException {
 Field field = new FieldTestDouble().getClass().getDeclaredField("nonGenericField2"); // replace with your non-generic field name
 field.setAccessible(true);

 new GenericMaster().getGenericType(field);
 }
}