package com.google.common.base;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Objects;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object(); // replace with actual object type and instance
 Object obj2 = obj1;

 // positive test case
 assertTrue(Objects.equal(obj1, obj2));

 Object obj3 = new Object(); // different instance
 assertFalse(Objects.equal(obj1, obj3));

 // null test case
 Object nullObj1 = null;
 Object nullObj2 = null;

 // comparing two nulls
 assertTrue(Objects.equal(nullObj1, nullObj2));

 // comparing null with non-null
 assertFalse(Objects.equal(nullObj1, obj1));
 }

 @Test
 public void testHashCode() {
 Object obj1 = new Object(); // replace with actual object type and instance
 Object obj2 = new Object(); // replace with actual object type and instance

 int hashCode1 = Objects.hashCode(obj1);
 int hashCode2 = Objects.hashCode(obj2);

 // two different objects should have different hashcodes
 assertNotSame(hashCode1, hashCode2);

 Object[] array = { obj1, obj2 };
 int hashCode3 = Objects.hashCode(array);

 // the hashcode of an array should be different from the hashcode of its elements
 assertNotSame(hashCode1, hashCode3);
 }

 @Test(expected = NullPointerException.class)
 public void testHashCodeWithNullObjects() {
 Object[] arrayWithNull = { obj1, null }; // replace with actual object type and instance

 try {
 Objects.hashCode(arrayWithNull);
 } catch (NullPointerException e) {
 // expected NullPointerException when null object is passed
 }
 }

 @Test
 public void testToStringHelper() {
 Object obj = new Object(); // replace with actual object type and instance

 String toString = Objects.toStringHelper(obj)
 .omitNullValues()
 .add("name1", "value1")
 .add("name2", obj)
 .toString();

 String expectedToString = "Object{name1=value1, name2=" + obj.getClass().getName() + "}";
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelperWithNullValues() {
 Object obj = new Object(); // replace with actual object type and instance

 String toString = Objects.toStringHelper(obj)
 .omitNullValues()
 .add("name1", null)
 .add("name2", obj)
 .toString();

 String expectedToString = "Object{name2=" + obj.getClass().getName() + "}";
 assertEquals(expectedToString, toString);
 }
}