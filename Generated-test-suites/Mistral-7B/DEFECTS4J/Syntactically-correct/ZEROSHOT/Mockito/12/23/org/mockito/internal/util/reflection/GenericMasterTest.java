package org.junit.junit4;

import org.junit.Test;
import org.mockito.internal.reflection.GenericMaster;
import org.mockito.internal.reflection.FieldTestDouble;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GenericMasterTest {

 private GenericMaster genericMaster = new GenericMaster();

 @Test
 public void testGetGenericType() throws NoSuchFieldException {
     // Test case 1: Simple field with no generic type
     Field simpleField = FieldTestDouble.class.getDeclaredField("simpleField");
     simpleField.setAccessible(true);
     assertEquals(Object.class, genericMaster.getGenericType(simpleField));

     // Test case 2: Simple field with generic type
     Field genericField = FieldTestDouble.class.getDeclaredField("genericField");
     simpleField.setAccessible(true);
     genericField.setAccessible(true);
     assertEquals(Integer.class, genericMaster.getGenericType(genericField));

     // Test case 3: Nested generic types
     Field nestedField = FieldTestDouble.class.getDeclaredField("nestedField");
     nestedField.setAccessible(true);
     assertEquals(java.util.List.class, genericMaster.getGenericType(nestedField.getType()));
     assertEquals(Integer.class, genericMaster.getGenericType(nestedField.getGenericType()));

     // Test case 4: Private field
     Field privateField = FieldTestDouble.class.getDeclaredField("privateField");
     privateField.setAccessible(true);
     assertEquals(Double.class, genericMaster.getGenericType(privateField));

     // Test case 5: Static field
     Field staticField = FieldTestDouble.class.getDeclaredField("staticField");
     staticField.setAccessible(true);
     staticField.setAccessible(true); // we must set accessible for the class as well
     assertEquals(String.class, genericMaster.getGenericType(staticField));

     // Test case 6: Final field
     Field finalField = FieldTestDouble.class.getDeclaredField("finalField");
     finalField.setAccessible(true);
     Modifier modifiers = finalField.getModifiers();
     modifiers |= Modifier.FINAL;
     finalField.setModifiers(modifiers);
     assertEquals(Integer.class, genericMaster.getGenericType(finalField));
 }

 static class FieldTestDouble {
     int simpleField;
     List<Integer> nestedField;
     Double privateField;
     static int staticField;
     public int genericField;

     public List<Double> getNestedField() {
         return Arrays.asList((Double) new Double(2.0));
     }
 }
}