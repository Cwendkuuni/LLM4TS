package org.junit.test;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestDouble;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GenericMasterTest {

 @Test
 public void testGetGenericType() throws NoSuchFieldException {
 // Create an instance of GenericMaster
 GenericMaster genericMaster = new GenericMaster();

 // Test case 1: Test field with a single generic type
 Field field1 = FieldTestDouble.class.getDeclaredField("field1");
 assertNotNull(field1);
 FieldTestDouble field1Instance = new FieldTestDouble();
 field1.setAccessible(true);
 field1.set(field1Instance, 10);
 Class expectedClass1 = Integer.TYPE;
 Class actualClass1 = genericMaster.getGenericType(field1);
 assertEquals(expectedClass1, actualClass1);

 // Test case 2: Test field with no generic type
 Field field2 = FieldTestDouble.class.getDeclaredField("field2");
 assertNotNull(field2);
 FieldTestDouble field2Instance = new FieldTestDouble();
 field2.setAccessible(true);
 field2.set(field2Instance, new Object());
 Class expectedClass2 = Object.class;
 Class actualClass2 = genericMaster.getGenericType(field2);
 assertEquals(expectedClass2, actualClass2);

 // Test case 3: Test field with nested generic types (not supported)
 Field field3 = FieldTestNested.class.getDeclaredField("field1");
 assertNotNull(field3);
 FieldTestNested field3Instance = new FieldTestNested();
 field3.setAccessible(true);
 field3.set(field3Instance, new Object[] { new Object(), new Object() });
 Class expectedClass3 = Object[].class;
 Class actualClass3 = genericMaster.getGenericType(field3);
 assertEquals(expectedClass3, actualClass3);
 }
}

class FieldTestDouble {
 private int field1;
 private Object field2;

 // getter and setter methods for the fields
}

class FieldTestNested {
 private Object[] field1;
}