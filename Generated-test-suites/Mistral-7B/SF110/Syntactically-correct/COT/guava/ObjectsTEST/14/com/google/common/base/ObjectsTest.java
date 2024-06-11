package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class ObjectsTest {

 // Test for equals method
 @Test
 public void testEquals() {
 Object obj1 = new Object();
 Object obj2 = obj1;
 Object obj3 = new Object();

 // Test with null objects
 assertTrue(Objects.equal(null, null));
 assertFalse(Objects.equal(null, obj1));
 assertFalse(Objects.equal(obj1, null));

 // Test with same object
 assertTrue(Objects.equal(obj1, obj2));

 // Test with different objects
 assertFalse(Objects.equal(obj1, obj3));
 }

 // Test for hashCode method
 @Test
 public void testHashCode() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 Object[] objects = {obj1, obj2};

 // Test with empty array
 assertEquals(0, Objects.hashCode(new Object[0]));

 // Test with single object
 assertEquals(Objects.hashCode(obj1), Objects.hashCode(new Object[]{obj1}));

 // Test with multiple objects
 assertEquals(Objects.hashCode(objects), Objects.hashCode(objects));
 }

 // Test for firstNonNull method (using MoreObjects.firstNonNull for deprecated method)
 @Test
 public void testFirstNonNull() {
 Object obj1 = null;
 Object obj2 = new Object();

 // Test with null first and non-null second
 assertNull(Objects.firstNonNull(obj1, obj2));

 // Test with non-null first and null second
 assertEquals(obj1, Objects.firstNonNull(obj1, obj2));

 // Test with both null
 assertNull(Objects.firstNonNull(null, null));

 // Test with both non-null
 assertEquals(obj1, Objects.firstNonNull(obj2, obj1));
 }

 // Test for ToStringHelper methods (using deprecated methods for simplicity)
 @Test
 public void testToStringHelper() {
 Object obj = new Object();
 Objects.ToStringHelper helper = Objects.toStringHelper(obj.getClass());

 // Test with default configuration
 assertEquals("Object{" + System.lineSeparator() + "}", helper.toString());

 // Test with omitNullValues()
 Objects.ToStringHelper helperNullValues = helper.omitNullValues();
 helperNullValues.add("name", obj);
 helperNullValues.add("booleanValue", true);
 helperNullValues.add("charValue", 'A');
 helperNullValues.add("doubleValue", 1.2);
 helperNullValues.add("floatValue", 1.2f);
 helperNullValues.add("intValue", 1);
 helperNullValues.add("longValue", 1L);
 helperNullValues.add("anotherObj", new Object());

 assertEquals("Object{" + System.lineSeparator() + "  booleanValue=true" + System.lineSeparator() + "  charValue='A'" + System.lineSeparator() + "  doubleValue=1.2" + System.lineSeparator() + "  floatValue=1.2" + System.lineSeparator() + "  intValue=1" + System.lineSeparator() + "  longValue=1" + System.lineSeparator() + "  anotherObj=", helperNullValues.toString());
 }

 // Test for edge cases and exceptions
 @Test(expected = NullPointerException.class)
 public void testToStringHelperNullClass() {
 Objects.ToStringHelper helper = Objects.toStringHelper(null);
 }

 @Test(expected = NullPointerException.class)
 public void testToStringHelperNullSelf() {
 Objects.ToStringHelper helper = Objects.toStringHelper(null);
 helper.add("self", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringHelperNullName() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Object.class);
 helper.add(null, "value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testToStringHelperNullValue() {
 Objects.ToStringHelper helper = Objects.toStringHelper(Object.class);
 helper.add("name", null);
 }
}