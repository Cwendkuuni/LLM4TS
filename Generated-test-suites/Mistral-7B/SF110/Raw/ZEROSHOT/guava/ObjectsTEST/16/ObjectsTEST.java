package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectsTest {

 @Test
 public void testEqual() {
 Object obj1 = new Object(); // Replace with actual object type and instance
 Object obj2 = obj1;
 assertTrue(Objects.equal(obj1, obj2));

 obj2 = new Object(); // Different instance
 assertFalse(Objects.equal(obj1, obj2));

 obj2 = obj1; // Same instance again
 assertTrue(Objects.equal(obj1, obj2));
 }

 @Test
 public void testHashCode() {
 Object[] objects = new Object[3];
 objects[0] = new Object();
 objects[1] = new Object();
 objects[2] = new Object();

 int hashCode = Objects.hashCode(objects);
 int expectedHashCode = Arrays.hashCode(objects);
 assertEquals(expectedHashCode, hashCode);
 }

 @Test
 public void testToStringHelper_omitNullValues_false() {
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassTest")
 .add("name1", "value1")
 .add("name2", null)
 .add("name3", "value3");
 String toString = toStringHelper.toString();
 String expectedToString = "ClassTest{name1=value1, name2=, name3=value3}";
 assertEquals(expectedToString, toString);
 }

 @Test
 public void testToStringHelper_omitNullValues_true() {
 Objects.ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassTest")
 .add("name1", "value1")
 .add("name2", null)
 .add("name3", "value3")
 .omitNullValues();
 String toString = toStringHelper.toString();
 String expectedToString = "ClassTest{name1=value1, name3=value3}";
 assertEquals(expectedToString, toString);
 }
}